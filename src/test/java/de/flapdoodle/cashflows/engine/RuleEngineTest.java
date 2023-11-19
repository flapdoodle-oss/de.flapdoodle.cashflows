package de.flapdoodle.cashflows.engine;

import de.flapdoodle.cashflows.generators.Ease;
import de.flapdoodle.cashflows.iterator.ForwardIterators;
import de.flapdoodle.cashflows.iterator.LinearIterators;
import de.flapdoodle.cashflows.tests.Area;
import de.flapdoodle.cashflows.tests.AsciiGraph;
import de.flapdoodle.cashflows.types.FlowState;
import de.flapdoodle.cashflows.types.Range;
import de.flapdoodle.cashflows.usecases.pv.SolarCalculations;
import de.flapdoodle.cashflows.usecases.types.KWh;
import de.flapdoodle.formula.Value;
import de.flapdoodle.formula.calculate.Calculate;
import de.flapdoodle.formula.rules.ImmutableRules;
import de.flapdoodle.formula.rules.Rules;
import de.flapdoodle.formula.values.Named;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.TimeZone;

class RuleEngineTest {
	@Test
	void sample() {
		LocalDateTime now = LocalDateTime.of(2012, 3, 24, 0, 0, 0, 0);

		Named<Integer> dayOfYearValue = Value.named("dayOfYear", Integer.class);
		Named<LocalDateTime> nowValue = Value.named("now", LocalDateTime.class);
		Named<String> dayOfYearAsStringValue = Value.named("dayOfYearAsString", String.class);

//		FlowId<Double> dummy = FlowId.of("dummy", FlowType.DOUBLE);

//		Range<LocalDate> eachDay = Range.of(now, it -> it.plusDays(1));
//		Range<LocalDate> eachMonday = Range.of(ForwardIterators.nextWeekday(now, DayOfWeek.MONDAY), it -> ForwardIterators.nextWeekday(it, DayOfWeek.MONDAY));

		Named<KWh> pvEnergy = Value.named("pv.energy", KWh.class);
		Named<KWh> houseConsumption = Value.named("house.consumption", KWh.class);
		Named<KWh> energyAfterConsumption = Value.named("energyAfterConsumption", KWh.class);
		Named<KWh> batteryStoredEnergy = Value.named("battery.storedEnergy", KWh.class);
		Named<KWh> batteryStoreEnergyDelta = Value.named("battery.storeEnergy.delta", KWh.class);
		Named<KWh> batteryMax = Value.named("battery.max", KWh.class);
		Named<KWh> grid = Value.named("grid", KWh.class);
		Named<KWh> gridImport = Value.named("gridImport", KWh.class);
		Named<KWh> gridExport = Value.named("gridExport", KWh.class);

		ImmutableRules rules = Rules.empty()
			.add(Calculate.value(pvEnergy)
				.requiring(nowValue)
				.by(n -> KWh.of(Ease.map(Ease.mirrorX(Ease::easeInOutQuad), n.getHour()/24.0, 0, 5.0))))
			.add(Calculate.value(houseConsumption)
				.by(() -> KWh.of(2)))
			.add(Calculate.value(batteryMax)
				.by(() -> KWh.of(6)))
			.add(Calculate.value(energyAfterConsumption)
				.requiring(pvEnergy, houseConsumption)
				.by(KWh::minus, "pv-consumption"))
			.add(Calculate.value(batteryStoreEnergyDelta)
				.requiring(batteryStoredEnergy, batteryMax, energyAfterConsumption)
				.by((current, max, delta) -> {
					if (delta.value()>0) {
						KWh maxDelta = max.minus(current);
						return maxDelta.value() > delta.value()
							? delta
							: maxDelta;
					} else {
						KWh newCurrent = current.plus(delta);
						return newCurrent.value() >= 0
							? delta
							: KWh.of(-current.value());
					}
				}))
			.add(Calculate.value(gridImport).requiring(energyAfterConsumption, batteryStoreEnergyDelta)
				.by((delta, batteryDelta) -> {
					KWh fromGrid = delta.minus(batteryDelta);
					return fromGrid.value() > 0
						? fromGrid
						: KWh.of(0);
				}))
			.add(Calculate.value(gridExport).requiring(energyAfterConsumption, batteryStoreEnergyDelta)
				.by((delta, batteryDelta) -> {
					KWh toGrid = delta.minus(batteryDelta);
					return toGrid.value() < 0
						? toGrid
						: KWh.of(0);
				}));


		RuleEngine<LocalDateTime> engine = RuleEngine.builder(LinearIterators.EACH_HOUR)
			.iteratorValue(nowValue)
			.iterationRules(rules)
			.addAggregations(Aggregation.of(KWh.of(0), batteryStoredEnergy, batteryStoreEnergyDelta, KWh::plus))
			.addAggregations(Aggregation.of(KWh.of(0), grid, gridExport, KWh::plus))
			.build();

		History<LocalDateTime> history = engine.run(now, now.plusDays(3));

		System.out.println(AsciiGraph.of(Area.of(0, 24*3-1, -1.0, 8.0)).render(24*3, 12, renderContext -> {
			for (int i = 0; i < 24*3; i++) {
				KWh v = history.get(i, batteryStoredEnergy);
				KWh g = history.get(i, grid);

//				System.out.println(">"+g.value());
				
//				FlowState<KWh> state = report.stateOf(pv, i);
				renderContext.point('*', i, v.value());
				renderContext.point('+', i, g.value());
//
//				FlowState<Double> cloudy = report.stateOf(isCloudy, i);
//				renderContext.point('#', i, cloudy.after()-cloudy.before());
			}
		}));
	}

	@Test
	void pvSample() {
		Calendar now = Calendar.getInstance();

		SolarCalculations c = new SolarCalculations(53.8, 10.37);
		System.out.println("Now: "+now);
		double az = c.calcSolarAzimuth(now);
		double decl = c.calcSolarDeclination(now);
		System.out.println("Az: "+az);
		System.out.println("Decl: "+decl);

		Named<Integer> hourOfDay = Value.named("hourOfDay", Integer.class);
		
		Rules.empty()
//			.add(Calculate.value())
		;
	}
}