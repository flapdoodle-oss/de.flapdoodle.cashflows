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
import static org.junit.jupiter.api.Assertions.*;

class LocationTest {

    @Test
    void sample() {
        LocalDate now = LocalDate.of(2023, Month.NOVEMBER, 1);

        ImmutableLocation testee = Location.builder()
                .localDateValue(now)
                .build();

        Rules rules = testee.rules();

        ValueGraph graph = ValueDependencyGraphBuilder.build(rules);

        String explained = RuleDependencyGraph.explain(rules);
        System.out.println("---------------------");
        System.out.println(explained);
        System.out.println("---------------------");


        Result solved = Solver.solve(graph, StrictValueLookup.of(
//                MappedValue.of(testee.dayOfTheYear(), 300),
//                MappedValue.of(testee.kwp(), KW.of(8.25))
        ));

        KWh energy = solved.get(Location.pvPerKWp());

        assertThat(energy.value()).isCloseTo(1.1049, Percentage.withPercentage(1));
    }

}