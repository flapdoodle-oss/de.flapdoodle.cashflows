package de.flapdoodle.cashflows.records;

import de.flapdoodle.cashflows.iterator.LinearIterators;
import de.flapdoodle.cashflows.types.*;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class RecordsTest {
	@Test
	public void aggregationSample() {
		LocalDate now = LocalDate.of(2012, 3, 24);

		FlowId<Double> a = FlowId.of("a", FlowType.DOUBLE);
		FlowId<Integer> b = FlowId.of("b", FlowType.INT);

		Records<LocalDate> records = Records.of(
				now,
				LinearIterators.EACH_DAY,
				Flow.of(a, 2.0),
				Flow.of(b, 1)
			).add(
				Record.of(a, 0, Change.of("first", 1.0)))
			.add(
				Record.of(a, 0, Change.of("second", 2.0))
			);

		assertThat(records.map())
			.containsOnlyKeys(a, b);

		ByFlowId<Double> byFlowId_a = (ByFlowId<Double>) records.map().get(a);

		assertThat(byFlowId_a)
			.extracting(ByFlowId::map, InstanceOfAssertFactories.map(Integer.class, ByIndex.class))
			.containsOnlyKeys(0);

		ByIndex<Double> byDate_a = byFlowId_a.map().get(0);

		assertThat(byDate_a.changes())
			.hasSize(2)
			.containsExactly(
				Change.of("first", 1.0),
				Change.of("second", 2.0)
			);

		assertThat(records.stateOf(a, 0))
			.isEqualTo(FlowState.of(2.0, 5.0));
	}
}