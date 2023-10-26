package de.flapdoodle.cashflows.engine;

import de.flapdoodle.cashflows.calculation.Calculation;
import de.flapdoodle.cashflows.calculation.Transaction;
import de.flapdoodle.cashflows.iterator.LinearIterators;
import de.flapdoodle.cashflows.records.Records;
import de.flapdoodle.cashflows.report.EachFlow2SystemOut;
import de.flapdoodle.cashflows.report.GroupByDate2SystemOut;
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

		Range<LocalDate> eachDay = Range.of(now, it -> it.plusDays(1));

		Engine<LocalDate> engine = Engine.builder(LinearIterators.EACH_DAY)
			.addFlows(Flow.of(dummy, 0d))
			.addTransactions(Transaction.<LocalDate>builder()
				.section(eachDay)
				.addCalculations(Calculation.of(dummy, (lastRun, n) -> Change.of("add", 10.0d)))
				.build())
			.addTransactions(Transaction.<LocalDate>builder()
				.section(Range.of(now.plusDays(1), it -> it.plusDays(2)))
				.addCalculations(Calculation.of(dummy, (lastRun, n) -> Change.of("sub", -5.0d)))
				.build())
			.build();

		Records<LocalDate> result = engine.calculate(now, now.plusDays(10));

		assertThat(result.stateOf(dummy, 10))
			.isEqualTo(FlowState.of(70.0, 75.0));

		new EachFlow2SystemOut<LocalDate>().render(result, Arrays.asList(dummy));
	}

	@Test
	void moveValuesBetweenFlows() {
		LocalDate now=LocalDate.of(2012,3,24);

		FlowId<Double> source = FlowId.of("source", FlowType.DOUBLE);
		FlowId<Double> dest = FlowId.of("dest", FlowType.DOUBLE);

		Range<LocalDate> eachDay = Range.of(now, it -> it.plusDays(1));

		Engine<LocalDate> engine = Engine.builder(LinearIterators.EACH_DAY)
			.addFlows(Flow.of(source, 500d))
			.addFlows(Flow.of(dest, 0d))
			.addTransactions(Transaction.<LocalDate>builder()
				.section(eachDay)
				.addCalculations(Calculation.of(dest, source, (lastRun, n, s) -> s.after() >= 100.0
					? Change.of("move 100 from source", 100.0)
					: Change.of("not enough", 0.0)))
				.addCalculations(Calculation.of(source, source, dest, (lastRun, n, s, d) -> s.after() >= 100.0
					? Change.of("fix moved 100", -100.0)
					: Change.of("dont fix", 0.0)))
				.build())
			.build();

		Records<LocalDate> result = engine.calculate(now, now.plusDays(10));

		assertThat(result.stateOf(source, 10))
			.isEqualTo(FlowState.of(0.0, 0.0));
		assertThat(result.stateOf(dest, 10))
			.isEqualTo(FlowState.of(500.0, 500.0));

		new GroupByDate2SystemOut<LocalDate>().render(result, Arrays.asList(source, dest));
	}
}