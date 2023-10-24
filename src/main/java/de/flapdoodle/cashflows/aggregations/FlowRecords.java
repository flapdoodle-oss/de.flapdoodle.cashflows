package de.flapdoodle.cashflows.aggregations;

import de.flapdoodle.cashflows.types.Flow;
import de.flapdoodle.cashflows.types.FlowId;
import de.flapdoodle.cashflows.types.FlowRecord;
import de.flapdoodle.cashflows.types.Maps;
import de.flapdoodle.checks.Preconditions;
import org.immutables.value.Value;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

@Value.Immutable
public abstract class FlowRecords {
	protected abstract Map<FlowId<?>, ByFlowId<?>> map();

	public <T> FlowRecords add(FlowRecord<T> record) {
		return ImmutableFlowRecords.builder()
			.map(Maps.changeEntry(map(), record.id(), byFlowId -> ((ByFlowId<T>) byFlowId).add(record)))
			.build();
	}

	public static FlowRecords of(Collection<? extends Flow<?>> flows) {
		return ImmutableFlowRecords.builder()
			.putAllMap(flows.stream().collect(Collectors.toMap(Flow::id, ByFlowId::of)))
			.build();
	}

	public static FlowRecords of(Flow<?>... flows) {
		return of(Arrays.asList(flows));
	}
}
