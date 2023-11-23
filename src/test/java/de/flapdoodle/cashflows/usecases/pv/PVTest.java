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
import de.flapdoodle.formula.calculate.MappedValue;
import de.flapdoodle.formula.calculate.StrictValueLookup;
import de.flapdoodle.formula.explain.RuleDependencyGraph;
import de.flapdoodle.formula.rules.Rules;
import de.flapdoodle.formula.solver.Result;
import de.flapdoodle.formula.solver.Solver;
import de.flapdoodle.formula.solver.ValueDependencyGraphBuilder;
import de.flapdoodle.formula.solver.ValueGraph;
import org.assertj.core.data.Percentage;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;

import static org.assertj.core.api.Assertions.assertThat;

class PVTest {

    @Test
    void sample() {
        ImmutablePV testee = PV.builder()
                .kwpValue(KW.of(8.25))
                .build();

        Rules rules = testee.rules(Rules.empty());

        ValueGraph graph = ValueDependencyGraphBuilder.build(rules);

        String explained = RuleDependencyGraph.explain(rules);
        System.out.println("---------------------");
        System.out.println(explained);
        System.out.println("---------------------");

        LocalDate now = LocalDate.of(2023, Month.NOVEMBER, 1);

        Result solved = Solver.solve(graph, StrictValueLookup.of(
                MappedValue.of(Location.localDate(), now),
                MappedValue.of(Location.pvPerKWp(), KWh.of(1.0))
//                MappedValue.of(testee.dayOfTheYear(), 300),
//                MappedValue.of(testee.kwp(), KW.of(8.25))
        ));

        KWh energy = solved.get(testee.energy());

        assertThat(energy.value()).isCloseTo(8.25, Percentage.withPercentage(1));
    }
}