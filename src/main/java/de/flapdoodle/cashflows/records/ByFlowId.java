package de.flapdoodle.cashflows.records;

import com.google.common.collect.ImmutableMap;
import de.flapdoodle.cashflows.types.Flow;
import de.flapdoodle.cashflows.types.FlowState;
import de.flapdoodle.cashflows.types.Maps;
import de.flapdoodle.checks.Preconditions;
import org.immutables.value.Value;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Value.Immutable
public abstract class ByFlowId<T> {
	@Value.Parameter
	public abstract Flow<T> flow();

	protected abstract Map<Integer, ByIndex<T>> map();

	@Value.Lazy
	public List<Integer> offsets() {
		return map().keySet().stream()
			.sorted()
			.collect(Collectors.toList());
	}

	@Value.Lazy
	protected Map<Integer, FlowState<T>> stateMap() {
		ImmutableMap.Builder<Integer, FlowState<T>> builder = ImmutableMap.builder();
		T current = flow().start();

		for (Integer date : offsets()) {
			Optional<T> delta = map().get(date).aggregatedDelta();
			T before = current;
			current = delta.isPresent()
				? flow().id().type().plus().apply(current, delta.get())
				: current;
			builder.put(date, FlowState.of(before, current));
		}

		return builder.build();
	}

	@Value.Auxiliary
	public FlowState<T> stateOf(Integer offset) {
		FlowState<T> state = stateMap().get(offset);
		if (state==null) {
			List<Integer> firstSmallerThanOffset = offsets().stream()
				.filter(it -> it <= offset).collect(Collectors.toList());
			if (!firstSmallerThanOffset.isEmpty()) {
				state = stateMap().get(firstSmallerThanOffset.get(firstSmallerThanOffset.size()-1));
			} else {
				state = FlowState.of(flow().start(), flow().start());
			}
		}
		return Preconditions.checkNotNull(state,"could not get state for %s", offset);
	}

	public ByIndex<T> get(Integer date) {
		return map().get(date);
	}

	public ByFlowId<T> add(Record<T> record) {
		if (map().containsKey(record.offset())) {
			return ImmutableByFlowId.<T>builder()
				.flow(flow())
				.map(Maps.changeEntry(map(), record.offset(), byDate -> byDate.add(record.change())))
				.build();
		} else {
			return ImmutableByFlowId.<T>builder()
				.from(this)
				.putMap(record.offset(), ByIndex.of(flow().id().type()).add(record.change()))
				.build();
		}
	}

	public static <T> ByFlowId<T> of(Flow<T> flow) {
		return ImmutableByFlowId.of(flow);
	}
}
