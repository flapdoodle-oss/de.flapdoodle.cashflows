package de.flapdoodle.cashflows.aggregations;

import de.flapdoodle.cashflows.types.*;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class FlowRecordsTest {

	@Test
	public void aggregationSample() {
		LocalDate now = LocalDate.of(2012, 3, 24);

		FlowId<Double> a = FlowId.of(FlowType.DOUBLE);
		FlowId<Integer> b = FlowId.of(FlowType.INT);

		FlowRecords records = FlowRecords.of(
			Flow.of(a, 2.0),
			Flow.of(b, 1)
		).add(
			FlowRecord.of(a, now, Change.of("first", 2.0))
		);

		assertThat(records.map())
			.containsOnlyKeys(a, b);

		ByFlowId<Double> byFlowId_a = (ByFlowId<Double>) records.map().get(a);

		assertThat(byFlowId_a)
			.extracting(ByFlowId::map, InstanceOfAssertFactories.map(LocalDate.class, ByDate.class))
			.containsOnlyKeys(now);

		ByDate<Double> byDate_a = byFlowId_a.map().get(now);

		assertThat(byDate_a.changes())
			.hasSize(1)
			.containsExactly(Change.of("first", 2.0));
	}
}