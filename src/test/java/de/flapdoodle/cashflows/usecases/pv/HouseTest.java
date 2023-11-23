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

import de.flapdoodle.cashflows.types.Id;
import de.flapdoodle.cashflows.usecases.types.KW;
import de.flapdoodle.cashflows.usecases.types.KWh;
import de.flapdoodle.formula.calculate.StrictValueLookup;
import de.flapdoodle.formula.explain.RuleDependencyGraph;
import de.flapdoodle.formula.rules.Rules;
import de.flapdoodle.formula.solver.Result;
import de.flapdoodle.formula.solver.Solver;
import de.flapdoodle.formula.solver.ValueDependencyGraphBuilder;
import de.flapdoodle.formula.solver.ValueGraph;
import de.flapdoodle.reflection.TypeInfo;
import org.assertj.core.data.Percentage;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;

import static org.assertj.core.api.Assertions.assertThat;

class HouseTest {

    @Test
    void sample() {
        LocalDate now = LocalDate.of(2023, Month.NOVEMBER, 1);

        ImmutablePV pv = PV.builder()
                .id(Id.of(TypeInfo.of(PV.class)).withAsHumanReadable("Id(pv0)"))
                .kwpValue(KW.of(8.25))
                .build();

        ImmutableLocation location = Location.builder()
                .localDateValue(now)
                .build();

        ImmutableHouse testee = House.builder()
                .summerConsumption(KWh.of(4))
                .winterConsumption(KWh.of(8))
                .pv(pv)
                .location(location)
                .build();

        Rules rules = testee.rules(Rules.empty());

        ValueGraph graph = ValueDependencyGraphBuilder.build(rules);

        String explained = RuleDependencyGraph.explain(rules);
        System.out.println("---------------------");
        System.out.println(explained);
        System.out.println("---------------------");


        Result solved = Solver.solve(graph, StrictValueLookup.of(
//                MappedValue.of(Location.dayOfTheYear(), 180)
//                MappedValue.of(testee.kwp(), KW.of(8.25))
        ));

        assertThat(solved.get(testee.consumption()).value())
          .isCloseTo(7.43, Percentage.withPercentage(1));

        assertThat(solved.get(testee.pv().get().energy()).value())
          .isCloseTo(9.11, Percentage.withPercentage(1));

        assertThat(solved.get(testee.toGrid()).value())
          .isCloseTo(1.68, Percentage.withPercentage(1));
    }
}