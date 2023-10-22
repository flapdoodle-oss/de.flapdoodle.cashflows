package de.flapdoodle.cashflows.engine;

import de.flapdoodle.cashflows.types.FlowId;
import de.flapdoodle.cashflows.types.FlowState;
import org.immutables.value.Value;

import java.util.Map;
import java.util.Optional;

@Value.Immutable
public abstract class FlowStateMap {
	protected abstract Map<FlowId<?>, FlowState<?>> map();

	@Value.Auxiliary
	public <T> Optional<FlowState<T>> get(FlowId<T> id) {
		return Optional.ofNullable((FlowState<T>) map().get(id));
	}

	public <T> FlowStateMap with(FlowId<T> id, FlowState<T> change) {
		return ImmutableFlowStateMap.builder().from(this)
			.putMap(id, change)
			.build();
	}

	public static FlowStateMap empty() {
		return ImmutableFlowStateMap.builder().build();
	}
}
