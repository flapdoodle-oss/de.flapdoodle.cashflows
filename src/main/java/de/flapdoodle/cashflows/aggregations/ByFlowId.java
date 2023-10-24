package de.flapdoodle.cashflows.aggregations;

import com.google.common.collect.ImmutableMap;
import de.flapdoodle.cashflows.types.Flow;
import de.flapdoodle.cashflows.types.FlowRecord;
import de.flapdoodle.cashflows.types.FlowState;
import de.flapdoodle.cashflows.types.Maps;
import de.flapdoodle.checks.Preconditions;
import org.immutables.value.Value;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Value.Immutable
public abstract class ByFlowId<T> {
	@Value.Parameter
	public abstract Flow<T> flow();

	protected abstract Map<LocalDate, ByDate<T>> map();

	@Value.Lazy
	protected List<LocalDate> dates() {
		return map().keySet().stream()
			.sorted()
			.collect(Collectors.toList());
	}

	@Value.Lazy
	protected Map<LocalDate, FlowState<T>> stateMap() {
		ImmutableMap.Builder<LocalDate, FlowState<T>> builder = ImmutableMap.builder();
		T current = flow().start();

		for (LocalDate date : dates()) {
			Optional<T> delta = map().get(date).aggregatedDelta();
			T before = current;
			current = delta.isPresent()
				? flow().id().type().reduce().apply(current, delta.get())
				: current;
			builder.put(date, FlowState.of(before, current));
		}

		return builder.build();
	}

	@Value.Auxiliary
	public FlowState<T> stateOf(LocalDate localDate) {
		return Preconditions.checkNotNull(stateMap().get(localDate),"could not get state for %s", localDate);
	}

	public ByFlowId<T> add(FlowRecord<T> record) {
		if (map().containsKey(record.date())) {
			return ImmutableByFlowId.<T>builder()
				.flow(flow())
				.map(Maps.changeEntry(map(), record.date(), byDate -> byDate.add(record.change())))
				.build();
		} else {
			return ImmutableByFlowId.<T>builder()
				.from(this)
				.putMap(record.date(), ByDate.of(flow().id().type()).add(record.change()))
				.build();
		}
	}

	public static <T> ByFlowId<T> of(Flow<T> flow) {
		return ImmutableByFlowId.of(flow);
	}
}
