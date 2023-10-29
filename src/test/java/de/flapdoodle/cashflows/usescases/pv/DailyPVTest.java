package de.flapdoodle.cashflows.usescases.pv;

import de.flapdoodle.cashflows.calculation.Calculation;
import de.flapdoodle.cashflows.calculation.Transaction;
import de.flapdoodle.cashflows.engine.Engine;
import de.flapdoodle.cashflows.generators.Generator;
import de.flapdoodle.cashflows.iterator.ForwardIterators;
import de.flapdoodle.cashflows.iterator.LinearIterators;
import de.flapdoodle.cashflows.records.Records;
import de.flapdoodle.cashflows.tests.Area;
import de.flapdoodle.cashflows.tests.AsciiGraph;
import de.flapdoodle.cashflows.types.*;
import de.flapdoodle.cashflows.usescases.types.KWh;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.temporal.ChronoUnit;

class DailyPVTest {

	@Test
	void dailyPV() {
		int dayOfTheYear = 302; // 29.10.2023

//		System.out.println("dayLength: "+ Sun.dayLength(dayOfTheYear));
//		System.out.println("sunshineHours: "+ Sun.sunshineHours(dayOfTheYear));
//		System.out.println("cloudyHours: "+ Sun.cloudyHours(dayOfTheYear));
//		System.out.println("pvPerKWp: "+ Sun.pvPerKWp(dayOfTheYear));
//		System.out.println("pvPerKWpPerSunshineHour: "+ Sun.pvPerKWpPerSunshineHour(dayOfTheYear));

		String result = AsciiGraph.of(Area.of(0, 365, 0.2, 0.4))
			.render(120, 12, renderContext -> {
				for (int i = 0; i < 365; i++) {
					renderContext.point('*', i, Sun.pvPerKWpPerSunshineHour(i).value());
				}
			});

//		System.out.println(result);

		Generator random = Generator.noise(1337, 24);

		FlowType<KWh> kWhFlowType = FlowType.of(KWh.class, KWh::plus, KWh::minus);
		FlowId<KWh> pv = FlowId.of("PV", kWhFlowType);


		LocalDateTime startOfDay = LocalDateTime.of(2023, Month.OCTOBER, 29, 0, 0, 0, 0);

		Engine<LocalDateTime> engine = Engine.builder(LinearIterators.EACH_HOUR)
			.addFlows(Flow.of(pv, KWh.of(0)))
			.addTransactions(Transaction.<LocalDateTime>builder()
				.section(Range.of(startOfDay, ForwardIterators.EACH_HOUR))
				.addCalculations(Calculation.of(pv, (lastRun, now) -> {
					int dayOfYear = now.getDayOfYear();

					int dayLen = (int) Sun.dayLength(dayOfYear);
					int start = (24 - dayLen) / 2;
					int end = start + dayLen;
					long hoursSinceLastRun = ChronoUnit.HOURS.between(lastRun, now);
					boolean isDay = start <= now.getHour() && now.getHour()<=end;
					int cloudHours = (int) Sun.cloudyHours(dayOfYear);
					System.out.println("cloudHoursToday: "+Generator.map(random, now.getHour()+24*dayOfTheYear, 0, cloudHours*2));
					
					if (isDay) {
						KWh kWPerKWp = Sun.pvPerKWpPerSunshineHour(dayOfYear);
						return Change.of("pv", kWPerKWp.multiply(8.25* hoursSinceLastRun));
					}
					return Change.of("pv", KWh.of(0));
				}))
				.build())
			.build();

		Records<LocalDateTime> report = engine.calculate(startOfDay, startOfDay.plusDays(1));

		System.out.println(AsciiGraph.of(Area.of(0, 23, 0, 10.0)).render(120, 12, renderContext -> {
			for (int i = 0; i < 24; i++) {
				FlowState<KWh> state = report.stateOf(pv, i);
				renderContext.point('*', i, state.after().value()-state.before().value());
			}
		}));
	}
}