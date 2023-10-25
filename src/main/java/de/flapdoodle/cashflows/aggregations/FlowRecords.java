package de.flapdoodle.cashflows.aggregations;

import de.flapdoodle.cashflows.calculation.FlowStateLookup;
import de.flapdoodle.cashflows.types.*;
import de.flapdoodle.checks.Preconditions;
import org.immutables.value.Value;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
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

	@Value.Lazy
	public List<LocalDate> dates() {
		return map().values().stream()
			.flatMap(it -> it.dates().stream())
			.collect(Collectors.toSet())
			.stream().sorted()
			.collect(Collectors.toList());
	}


	@Value.Auxiliary
	public <T> FlowState<T> stateOf(FlowId<T> id, LocalDate date) {
		return get(id).stateOf(date);
	}

	@Value.Auxiliary
	public FlowStateLookup stateLookupOf(LocalDate transactionLastRun, LocalDate current) {
		return new FlowStateLookup() {
			@Override
			public <T> FlowState<T> stateOf(FlowId<T> id) {
				FlowState<T> lastState = FlowRecords.this.stateOf(id, transactionLastRun);
				FlowState<T> currentState = FlowRecords.this.stateOf(id, current);
				return FlowState.of(lastState.before(), currentState.after());
			}
		};
	}

	public <T> ByFlowId<T> get(FlowId<T> id) {
		return Preconditions.checkNotNull((ByFlowId<T>) map().get(id),"could not get entry for %s", id);
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
