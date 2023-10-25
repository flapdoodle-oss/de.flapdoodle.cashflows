package de.flapdoodle.cashflows.engine;

import de.flapdoodle.cashflows.aggregations.ByDate;
import de.flapdoodle.cashflows.aggregations.ByFlowId;
import de.flapdoodle.cashflows.aggregations.FlowRecords;
import de.flapdoodle.cashflows.calculation.Calculation;
import de.flapdoodle.cashflows.report.EachFlow2SystemOut;
import de.flapdoodle.cashflows.report.GroupByDate2SystemOut;
import de.flapdoodle.cashflows.types.Change;
import de.flapdoodle.cashflows.calculation.Transaction;
import de.flapdoodle.cashflows.types.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;

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

		new EachFlow2SystemOut().render(result, Arrays.asList(dummy));
	}

	@Test
	void moveValuesBetweenFlows() {
		LocalDate now=LocalDate.of(2012,3,24);

		FlowId<Double> source = FlowId.of("source", FlowType.DOUBLE);
		FlowId<Double> dest = FlowId.of("dest", FlowType.DOUBLE);

		DateRange eachDay = DateRange.of(now, it -> it.plusDays(1));

		Engine engine = Engine.builder()
			.addFlows(Flow.of(source, 500d))
			.addFlows(Flow.of(dest, 0d))
			.addTransactions(Transaction.builder()
				.section(eachDay)
				.addCalculations(Calculation.of(dest, source, (s, duration) -> s.after() >= 100.0
					? Change.of("move 100 from source", 100.0)
					: Change.of("not enough", 0.0)))
				.addCalculations(Calculation.of(source, source, dest, (s, d, duration) -> s.after() >= 100.0
						? Change.of("fix moved 100", -100.0)
						: Change.of("dont fix", 0.0)))
				.build())
			.build();

		FlowRecords result = engine.calculate(now, now.plusDays(10));

//		assertThat(result.stateOf(source, now.plusDays(10)))
//			.isEqualTo(FlowState.of(55.0, 60.0));

		new GroupByDate2SystemOut().render(result, Arrays.asList(source, dest));
	}
}