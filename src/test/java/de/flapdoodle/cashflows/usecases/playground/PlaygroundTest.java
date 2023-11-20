package de.flapdoodle.cashflows.usecases.playground;

import de.flapdoodle.cashflows.usecases.types.KWh;
import de.flapdoodle.formula.Value;
import de.flapdoodle.formula.calculate.Calculate;
import de.flapdoodle.formula.calculate.MappedValue;
import de.flapdoodle.formula.calculate.StrictValueLookup;
import de.flapdoodle.formula.explain.RuleDependencyGraph;
import de.flapdoodle.formula.rules.ImmutableRules;
import de.flapdoodle.formula.rules.Rules;
import de.flapdoodle.formula.solver.Result;
import de.flapdoodle.formula.solver.Solver;
import de.flapdoodle.formula.solver.ValueDependencyGraphBuilder;
import de.flapdoodle.formula.solver.ValueGraph;
import de.flapdoodle.formula.values.Named;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PlaygroundTest {
	
	@Test
	void doIt() {
		Named<KWh> pvEnergy = Value.named("pv.energy", KWh.class);
		Named<KWh> houseConsumption = Value.named("house.consumption", KWh.class);
		Named<KWh> energyAfterConsumption = Value.named("energyAfterConsumption", KWh.class);
		Named<KWh> batteryStoredEnergy = Value.named("battery.storedEnergy", KWh.class);
		Named<KWh> batteryStoreEnergyDelta = Value.named("battery.storeEnergy.delta", KWh.class);
		Named<KWh> batteryMax = Value.named("battery.max", KWh.class);
		Named<KWh> gridImport = Value.named("gridImport", KWh.class);
		Named<KWh> gridExport = Value.named("gridExport", KWh.class);

		ImmutableRules rules = Rules.empty()
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

		ValueGraph graph = ValueDependencyGraphBuilder.build(rules);

		String explained = RuleDependencyGraph.explain(rules);
		System.out.println("---------------------");
		System.out.println(explained);
		System.out.println("---------------------");


		Result solved = Solver.solve(graph, StrictValueLookup.of(
			MappedValue.of(pvEnergy, KWh.of(9.25)),
			MappedValue.of(houseConsumption, KWh.of(12.0)),
			MappedValue.of(batteryStoredEnergy, KWh.of(4.0)),
			MappedValue.of(batteryMax, KWh.of(7.65))
		));

		assertThat(solved.get(batteryStoreEnergyDelta))
			.isEqualTo(KWh.of(-2.75));

		assertThat(solved.get(gridImport))
			.isEqualTo(KWh.of(0));

		assertThat(solved.get(gridExport))
			.isEqualTo(KWh.of(0));
	}
}
