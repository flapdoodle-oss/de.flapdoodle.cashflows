package de.flapdoodle.cashflows.usecases.pv;

import de.flapdoodle.cashflows.usecases.types.KW;
import de.flapdoodle.cashflows.usecases.types.KWh;
import de.flapdoodle.formula.Value;
import de.flapdoodle.formula.calculate.Calculation;
import de.flapdoodle.formula.calculate.MappedValue;
import de.flapdoodle.formula.calculate.StrictValueLookup;
import de.flapdoodle.formula.calculate.ValueLookup;
import de.flapdoodle.formula.explain.RuleDependencyGraph;
import de.flapdoodle.formula.rules.Rules;
import de.flapdoodle.formula.solver.*;
import de.flapdoodle.formula.values.Named;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.assertj.core.data.Percentage;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;

import static org.assertj.core.api.Assertions.assertThat;

class HomeTest {
	@Test
	void doesOverrideWorks() {
		Named<KW> kwp = Value.named("pv KWp(2)", KW.class);

		Home testee = new Home() {
			@Override
			public Named<KW> kwp() {
				return kwp;
			}
		};

		Rules rules = testee.rules();

		Calculation<KWh> calculation = rules.calculations().get(testee.pv());

		assertThat(calculation.sources())
			.isNotNull()
			.asInstanceOf(InstanceOfAssertFactories.collection(Named.class))
			.containsExactlyInAnyOrder(testee.pvPerKWp(), kwp);

		ValueGraph graph = ValueDependencyGraphBuilder.build(rules);

		String explained = RuleDependencyGraph.explain(rules);
		System.out.println("---------------------");
		System.out.println(explained);
		System.out.println("---------------------");

		Result solved = Solver.solve(graph, StrictValueLookup.of(
			MappedValue.of(testee.dayOfTheYear(), 300),
			MappedValue.of(testee.kwp(), KW.of(8.25)),
			MappedValue.of(testee.summerConsumption(), KWh.of(4.00)),
			MappedValue.of(testee.winterConsumption(), KWh.of(4.00))
		));

		KWh pv = solved.get(testee.pv());

		assertThat(pv.value()).isCloseTo(10.174056103241979, Percentage.withPercentage(99));
		
	}

}