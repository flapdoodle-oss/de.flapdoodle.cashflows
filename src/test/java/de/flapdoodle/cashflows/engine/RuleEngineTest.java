/*
 * Copyright (C) 2022
 *   Michael Mosmann <michael@mosmann.de>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.flapdoodle.cashflows.engine;

import de.flapdoodle.cashflows.generators.Ease;
import de.flapdoodle.cashflows.iterator.LinearIterators;
import de.flapdoodle.cashflows.records.History;
import de.flapdoodle.cashflows.report.History2Csv;
import de.flapdoodle.cashflows.tests.Area;
import de.flapdoodle.cashflows.tests.AsciiGraph;
import de.flapdoodle.cashflows.usecases.pv.SolarCalculations;
import de.flapdoodle.cashflows.usecases.types.KWh;
import de.flapdoodle.formula.Value;
import de.flapdoodle.formula.calculate.Calculate;
import de.flapdoodle.formula.rules.ImmutableRules;
import de.flapdoodle.formula.rules.Rules;
import de.flapdoodle.formula.values.Named;
import de.flapdoodle.reflection.TypeInfo;
import org.junit.jupiter.api.Test;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;

class RuleEngineTest {
	@Test
	void sample() {
		LocalDateTime now = LocalDateTime.of(2012, 3, 24, 0, 0, 0, 0);

		Named<Integer> dayOfYearValue = Value.named("dayOfYear", Integer.class);
		Named<Iteration<LocalDateTime>> nowValue = Value.named("now", Iteration.typeInfo(TypeInfo.of(LocalDateTime.class)));
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
				.by(n -> KWh.of(Ease.map(Ease.mirrorX(Ease::easeInOutQuad), n.current().getHour()/24.0, 0, 5.0))))
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

		String csv = new History2Csv<LocalDateTime>(RuleEngineTest::toCSV)
			.render(history, Arrays.asList(batteryStoredEnergy, grid));
		System.out.println("-------------------");
		System.out.println(csv);
		System.out.println("-------------------");
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

	private static DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd.MM.yyyy");
	private static DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
	private static NumberFormat NUMBER_FORMAT = NumberFormat.getInstance(Locale.GERMAN);

	private static String toCSV(Object o) {
		if (o instanceof KWh) {
			return asString(NUMBER_FORMAT.format(((KWh) o).value()));
		}
		if (o instanceof LocalDate) {
			LocalDate d = (LocalDate) o;
			return asString(DATE_FORMAT.format(d));
		}
		if (o instanceof LocalDateTime) {
			LocalDateTime d = (LocalDateTime) o;
			return asString(DATE_TIME_FORMAT.format(d));
		}
		return o.toString();
	}

	private static String asString(String csvValue) {
		return "\""+csvValue+"\"";
	}

}