package de.flapdoodle.cashflows.usescases.pv;

import de.flapdoodle.cashflows.calculation.Calculation;
import de.flapdoodle.cashflows.calculation.Transaction;
import de.flapdoodle.cashflows.engine.Engine;
import de.flapdoodle.cashflows.engine.ImmutableEngine;
import de.flapdoodle.cashflows.generators.Ease;
import de.flapdoodle.cashflows.iterator.ForwardIterators;
import de.flapdoodle.cashflows.iterator.LinearIterators;
import de.flapdoodle.cashflows.records.Records;
import de.flapdoodle.cashflows.report.GroupByDate2SystemOut;
import de.flapdoodle.cashflows.tests.Area;
import de.flapdoodle.cashflows.tests.AsciiGraph;
import de.flapdoodle.cashflows.types.*;
import de.flapdoodle.cashflows.usescases.types.KWh;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;

public class CarsTest {

	@Test
	void comparIt() {
//		LocalDateTime startOfDay = LocalDateTime.of(2023, Month.OCTOBER, 29, 0, 0, 0, 0);

		FlowType<KWh> kWhFlowType = FlowType.of(KWh.class, KWh::plus, KWh::minus);
		FlowId<KWh> pv = FlowId.of("PV", kWhFlowType);
		FlowId<KWh> netz = FlowId.of("Netz", kWhFlowType);
		FlowId<KWh> haushalt = FlowId.of("Haushalt", kWhFlowType);
		FlowId<KWh> wallbox = FlowId.of("Wallbox", kWhFlowType);
		FlowId<KWh> auto = FlowId.of("Auto", kWhFlowType);

//		String result = AsciiGraph.of(Area.of(0, 365, 0.2, 5.4))
//			.render(120, 12, renderContext -> {
//				for (int i = 0; i < 365; i++) {
//					renderContext.point('*', i, Sun.pvPerKWp(i).value());
//				}
//			});
//
//		System.out.println(result);

		LocalDate start = LocalDate.of(2023, Month.NOVEMBER, 1);

		Engine<LocalDate> engine = Engine.builder(LinearIterators.EACH_DAY)
			.addFlows(Flow.of(pv, KWh.of(0)))
			.addFlows(Flow.of(netz, KWh.of(0)))
			.addFlows(Flow.of(haushalt, KWh.of(0)))
			.addFlows(Flow.of(wallbox, KWh.of(0)))
			.addTransactions(Transaction.<LocalDate>builder()
				.section(Range.of(start, ForwardIterators.EACH_DAY))
				.addCalculations(Calculation.of(pv, (lastRun, now) -> Change.of("sonne", Sun.pvPerKWp(now.getDayOfYear()).multiply(8.25))))
				.addCalculations(
					Calculation.of(haushalt, (lastRun, now) -> {
						double consumption = Ease.map(Ease.mirrorX(Ease::easeInOutCubic), now.getDayOfYear() / 365.0, 6.0, 3.0);
						return Change.of("verbrauch", KWh.of(consumption));
					}))
//				.addCalculations(Calculation.of())
				.build())
			.build();

		Records<LocalDate> records = engine.calculate(start, start.plusYears(1));

//		new GroupByDate2SystemOut<LocalDate>()
//			.render(records, Arrays.asList(pv, haushalt));

		String result = AsciiGraph.of(Area.of(0, 365, 0.0, 30.0))
			.render(120, 12, renderContext -> {
				records.offsets().forEach(dayOff -> {
					FlowState<KWh> sun = records.stateOf(pv, dayOff);
					renderContext.point('*', dayOff, sun.after().minus(sun.before()).value());
					FlowState<KWh> h = records.stateOf(haushalt, dayOff);
					renderContext.point('#', dayOff, h.after().minus(h.before()).value());
				});
			});

		System.out.println(result);
	}
}
