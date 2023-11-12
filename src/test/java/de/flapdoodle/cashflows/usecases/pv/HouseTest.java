package de.flapdoodle.cashflows.usecases.pv;

import de.flapdoodle.cashflows.types.Id;
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
                MappedValue.of(Location.dayOfTheYear(), 300)
//                MappedValue.of(testee.kwp(), KW.of(8.25))
        ));

        KWh energy = solved.get(testee.consumption());

        assertThat(energy.value()).isCloseTo(4.568, Percentage.withPercentage(1));
    }
}