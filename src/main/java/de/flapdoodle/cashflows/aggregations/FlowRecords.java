package de.flapdoodle.cashflows.aggregations;

import de.flapdoodle.cashflows.types.*;
import de.flapdoodle.checks.Preconditions;
import org.immutables.value.Value;

import java.time.LocalDate;
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

	public <T> FlowState<T> stateOf(FlowId<T> id, LocalDate date) {
		return Preconditions.checkNotNull((ByFlowId<T>)map().get(id),"").stateOf(date);
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
