package de.flapdoodle.cashflows.engine;

import de.flapdoodle.cashflows.calculation.Calculation;
import de.flapdoodle.cashflows.calculation.FlowChange;
import de.flapdoodle.cashflows.calculation.Transaction;
import de.flapdoodle.cashflows.types.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class EngineTest {
	@Test
	void sample() {
		LocalDate now=LocalDate.of(2012,3,24);

		FlowId<Double> dummy = FlowId.of(FlowType.DOUBLE);

		DateRange eachDay = DateRange.of(now, it -> it.plusDays(1));

		Engine engine = Engine.builder()
			.addFlows(Flow.of(dummy, 0d))
			.addTransactions(Transaction.builder()
				.section(eachDay)
				.addCalculations(Calculation.of(dummy, duration -> FlowChange.of("add", 10.0d)))
				.build())
			.build();

		FlowStates result = engine.calculate(now, now.plusDays(10));

		assertThat(result.get(now.plusDays(10)).get(dummy))
			.isPresent()
			.contains(FlowState.of(100.0, 110.0));
	}
}