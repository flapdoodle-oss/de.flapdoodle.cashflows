package de.flapdoodle.cashflows.engine;

import de.flapdoodle.cashflows.aggregations.ByDate;
import de.flapdoodle.cashflows.aggregations.ByFlowId;
import de.flapdoodle.cashflows.aggregations.FlowRecords;
import de.flapdoodle.cashflows.calculation.Calculation;
import de.flapdoodle.cashflows.types.Change;
import de.flapdoodle.cashflows.calculation.Transaction;
import de.flapdoodle.cashflows.types.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class EngineTest {
	@Test
	void sample() {
		LocalDate now=LocalDate.of(2012,3,24);

		FlowId<Double> dummy = FlowId.of("dummy", FlowType.DOUBLE);

		DateRange eachDay = DateRange.of(now, it -> it.plusDays(1));

		Engine engine = Engine.builder()
			.addFlows(Flow.of(dummy, 0d))
			.addTransactions(Transaction.builder()
				.section(eachDay)
				.addCalculations(Calculation.of(dummy, duration -> Change.of("add", 10.0d)))
				.build())
			.addTransactions(Transaction.builder()
				.section(DateRange.of(now.plusDays(1), it -> it.plusDays(2)))
				.addCalculations(Calculation.of(dummy, duration -> Change.of("sub", -5.0d)))
				.build())
			.build();

		FlowRecords result = engine.calculate(now, now.plusDays(10));

		assertThat(result.stateOf(dummy, now.plusDays(10)))
			.isEqualTo(FlowState.of(55.0, 60.0));

		render(result, dummy);
	}

	private void render(FlowRecords records, FlowId<?> ... flowIds) {
		for (FlowId<?> id : flowIds) {
			System.out.println(id.id()+": "+id.type().type());
			ByFlowId<?> byFlowId = records.get(id);
			for (LocalDate date : byFlowId.dates()) {
				System.out.println("  "+date);
				ByDate<?> byDate = byFlowId.get(date);
				for (Change<?> change : byDate.changes()) {
					System.out.println("    "+change.name()+": "+change.delta());
				}
			}
		}
	}
}