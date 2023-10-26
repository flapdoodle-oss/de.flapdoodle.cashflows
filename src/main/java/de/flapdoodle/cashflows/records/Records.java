package de.flapdoodle.cashflows.records;

import de.flapdoodle.cashflows.calculation.FlowStateLookup;
import de.flapdoodle.cashflows.iterator.LinearIterator;
import de.flapdoodle.cashflows.types.Flow;
import de.flapdoodle.cashflows.types.FlowId;
import de.flapdoodle.cashflows.types.FlowState;
import de.flapdoodle.cashflows.types.Maps;
import de.flapdoodle.checks.Preconditions;
import org.immutables.value.Value;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Value.Immutable
public abstract class Records<I> {
	protected abstract I start();
	protected abstract LinearIterator<I> iterator();
	protected abstract Map<FlowId<?>, ByFlowId<?>> map();

	public <T> Records<I> add(Record<T> record) {
		return ImmutableRecords.<I>builder()
			.start(start())
			.iterator(iterator())
			.map(Maps.changeEntry(map(), record.id(), byFlowId -> ((ByFlowId<T>) byFlowId).add(record)))
			.build();
	}

	@Value.Auxiliary
	public I mapOffset(int offset) {
		return iterator().next(start(), offset);
	}

	@Value.Lazy
	public List<Integer> offsets() {
		return map().values().stream()
			.flatMap(it -> it.offsets().stream())
			.collect(Collectors.toSet())
			.stream().sorted()
			.collect(Collectors.toList());
	}


	@Value.Auxiliary
	public <T> FlowState<T> stateOf(FlowId<T> id, Integer offset) {
		return get(id).stateOf(offset);
	}

	@Value.Auxiliary
	public FlowStateLookup stateLookupOf(int transactionLastRun, int current) {
		return new FlowStateLookup() {
			@Override
			public <T> FlowState<T> stateOf(FlowId<T> id) {
				FlowState<T> lastState = Records.this.stateOf(id, transactionLastRun);
				FlowState<T> currentState = Records.this.stateOf(id, current);
				return FlowState.of(lastState.before(), currentState.after());
			}
		};
	}

	public <T> ByFlowId<T> get(FlowId<T> id) {
		return Preconditions.checkNotNull((ByFlowId<T>) map().get(id),"could not get entry for %s", id);
	}

	public static <T> Records<T> of(T start, LinearIterator<T> iterator, Collection<? extends Flow<?>> flows) {
		return ImmutableRecords.<T>builder()
			.start(start)
			.iterator(iterator)
			.putAllMap(flows.stream().collect(Collectors.toMap(Flow::id, ByFlowId::of)))
			.build();
	}

	public static <T> Records<T> of(T start, LinearIterator<T> iterator, Flow<?>... flows) {
		return of(start, iterator, Arrays.asList(flows));
	}
}
