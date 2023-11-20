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