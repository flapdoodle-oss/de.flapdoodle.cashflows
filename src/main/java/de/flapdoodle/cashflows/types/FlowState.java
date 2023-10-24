package de.flapdoodle.cashflows.types;

import org.immutables.value.Value;

@Value.Immutable
public interface FlowState<T> extends HasFlowStateChange<T> {
	@Override
	T before();
	
	@Override
	T after();

	static <T> FlowState<T> of(T before, T after) {
		return ImmutableFlowState.<T>builder()
			.before(before)
			.after(after)
			.build();
	}
}
