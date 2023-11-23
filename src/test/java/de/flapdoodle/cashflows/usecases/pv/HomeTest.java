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
package de.flapdoodle.cashflows.usecases.pv;

import de.flapdoodle.cashflows.usecases.types.KW;
import de.flapdoodle.cashflows.usecases.types.KWh;
import de.flapdoodle.formula.Value;
import de.flapdoodle.formula.calculate.Calculation;
import de.flapdoodle.formula.calculate.MappedValue;
import de.flapdoodle.formula.calculate.StrictValueLookup;
import de.flapdoodle.formula.explain.RuleDependencyGraph;
import de.flapdoodle.formula.rules.Rules;
import de.flapdoodle.formula.solver.Result;
import de.flapdoodle.formula.solver.Solver;
import de.flapdoodle.formula.solver.ValueDependencyGraphBuilder;
import de.flapdoodle.formula.solver.ValueGraph;
import de.flapdoodle.formula.values.Named;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.assertj.core.data.Percentage;
import org.junit.jupiter.api.Test;

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