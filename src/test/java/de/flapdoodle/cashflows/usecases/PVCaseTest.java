package de.flapdoodle.cashflows.usecases;

import de.flapdoodle.cashflows.calculation.Calculation;
import de.flapdoodle.cashflows.calculation.Transaction;
import de.flapdoodle.cashflows.generators.Ease;
import de.flapdoodle.cashflows.engine.Engine;
import de.flapdoodle.cashflows.iterator.ForwardIterators;
import de.flapdoodle.cashflows.iterator.LinearIterators;
import de.flapdoodle.cashflows.records.Records;
import de.flapdoodle.cashflows.report.GroupByDate2Csv;
import de.flapdoodle.cashflows.report.GroupByDate2SystemOut;
import de.flapdoodle.cashflows.types.*;
import org.assertj.core.data.Percentage;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;

public class PVCaseTest {

	@Test
	public void dummy() {
		LocalDate now = LocalDate.of(2023, Month.OCTOBER, 23);

		FlowType<KWh> kWhFlowType = FlowType.of(KWh.class, KWh::plus, KWh::minus);

		FlowId<KWh> pv = FlowId.of("PV", kWhFlowType);
		FlowId<KWh> grid = FlowId.of("Grid", kWhFlowType);
		FlowId<KWh> battery = FlowId.of("Battery", kWhFlowType);
		FlowId<KWh> houseConsumption = FlowId.of("Consumption", kWhFlowType);

		LocalDateTime startOfDay = LocalDateTime.of(2023, Month.OCTOBER, 23, 0, 0, 0, 0);

		Engine<LocalDateTime> pvEngine = Engine.builder(LinearIterators.EACH_HOUR)
			.addFlows(Flow.of(pv, KWh.of(0)))
			.addTransactions(Transaction.<LocalDateTime>builder()
				.section(Range.of(startOfDay, ForwardIterators.EACH_HOUR))
				.addCalculations(Calculation.of(pv, (lastRun, n) -> {
					int start = 6;
					int end = 18;
					int noon = 12;
					int currentHour = n.getHour();
					double maxKWh = 8.25;

					KWh ret = KWh.of(0);
					if (start <= currentHour && currentHour <= 18) {
						double factor = Math.sin(Math.PI * (currentHour - start) / (end - start));
						ret = KWh.of(factor * maxKWh);
					}

					return Change.of("pv at " + n.getHour() + ":00", ret);
				}))
				.build())
			.build();

		Records<LocalDateTime> pvReport = pvEngine.calculate(startOfDay, startOfDay.plusHours(24));

		new GroupByDate2SystemOut<LocalDateTime>()
			.render(pvReport, Arrays.asList(pv));
	}

	@Test
	public void simulation() {
		LocalDate now = LocalDate.of(2023, Month.SEPTEMBER, 1);
		FlowType<KWh> kWhFlowType = FlowType.of(KWh.class, KWh::plus, KWh::minus);

		FlowId<KWh> pv = FlowId.of("PV", kWhFlowType);
		FlowId<KWh> consumption = FlowId.of("Consumption", kWhFlowType);
		FlowId<KWh> export = FlowId.of("Export", kWhFlowType);

		Engine<LocalDate> pvEngine = Engine.builder(LinearIterators.EACH_DAY)
			.addFlows(
				Flow.of(pv, KWh.of(0)),
				Flow.of(consumption, KWh.of(0)),
				Flow.of(export, KWh.of(0))
			)
			.addTransactions(Transaction.<LocalDate>builder()
				.section(Range.of(now, ForwardIterators.EACH_DAY))
				.addCalculations(pvPerDay(pv, KW.of(8.25)))
				.build())
			.addTransactions(Transaction.<LocalDate>builder()
				.section(Range.of(now, ForwardIterators.EACH_DAY))
				.addCalculations(consumptionPerDay(consumption, KWh.of(5.0), KWh.of(8.0)))
				.build())
			.addTransactions(Transaction.<LocalDate>builder()
				.section(Range.of(now, ForwardIterators.EACH_DAY))
				.addCalculations(Calculation.of(export, pv, consumption, (lastRun, n, pvState, consumptionState) -> {
					KWh pvProduced = pvState.after().minus(pvState.before());
					KWh consumed = consumptionState.after().minus(consumptionState.before());
					KWh canExport = pvProduced.minus(consumed);
					return Change.of("export", canExport.value() >= 0.0 ? canExport : KWh.of(0.0));
				}))
				.build())
			.build();

		Records<LocalDate> pvReport = pvEngine.calculate(now, now.plusYears(1));

		GroupByDate2Csv renderer = new GroupByDate2Csv(PVCaseTest::toCSV);
		renderer.render(pvReport, Arrays.asList(pv, consumption, export));
		String csv = renderer.renderResult();

		System.out.println("-----------------");
		System.out.println(csv);
		System.out.println("-----------------");
	}

	private static DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd.MM.yyyy");
	private static NumberFormat NUMBER_FORMAT = NumberFormat.getInstance(Locale.GERMAN);

	private static String toCSV(Object o) {
		if (o instanceof KWh) {
			return NUMBER_FORMAT.format(((KWh) o).value());
		}
		if (o instanceof LocalDate) {
			LocalDate d = (LocalDate) o;
			return DATE_FORMAT.format(d);
		}
		return o.toString();
	}

	@Test
	public void pvOverYear() {
		assertThat(pv(KW.of(8.25), 0).value())
			.isCloseTo(2.3017, Percentage.withPercentage(1.0));
		assertThat(pv(KW.of(8.25), 180).value())
			.isCloseTo(30.24, Percentage.withPercentage(1.0));
	}

	private static Calculation<LocalDate, KWh> pvPerDay(FlowId<KWh> dest, KW peak) {
		return Calculation.of(dest, (LocalDate lastRun, LocalDate now) -> Change.of("pv", pv(peak, now.getDayOfYear())));
	}

	private static Calculation<LocalDate, KWh> consumptionPerDay(FlowId<KWh> dest, KWh summer, KWh winterKWhday) {
		return Calculation.of(dest, (LocalDate lastRun, LocalDate now) -> Change.of("consumtion", consumption(summer, winterKWhday, now.getDayOfYear())));
	}

	// Beispiele aus dem Netz
	// 10kWp -> 1200kWh pro Monat - Sommer
	// 10kWp -> 837kWh pro Monat - Winter
	private static KWh pv(KW peakKW, int dayOfYear) {
		double maxkWhproKW = 110.0 / 30.0;
		double minkWhproKW = 8.37 / 30.0;
		return KWh.of(Sun.normal(dayOfYear, minkWhproKW * peakKW.value(), maxkWhproKW * peakKW.value()));
	}

	private static KWh consumption(KWh minKWday, KWh maxKWday, int dayOfYear) {
		double min = maxKWday.value();
		double max = minKWday.value();
		return KWh.of(Ease.map(Ease.mirrorX(Ease::easeInOutCubic), dayOfYear / 365.0, min, max));
	}

	@Nested
	class Base {

		@Test
		void sunFactor() {
			assertThat(Sun.normal(0, 5.0, 10.0))
				.isEqualTo(5.0);
			assertThat(Sun.normal(365 / 2, 5.0, 10.0))
				.isCloseTo(10.0, Percentage.withPercentage(0.01));

			assertThat(Sun.normal(0, 10.0, 5.0))
				.isEqualTo(10.0);
			assertThat(Sun.normal(365 / 2, 10.0, 5.0))
				.isCloseTo(5.0, Percentage.withPercentage(0.01));
		}
	}

	// 7.5 -> 17
	//public static double sin(double min, double max, int current, int max)
}
