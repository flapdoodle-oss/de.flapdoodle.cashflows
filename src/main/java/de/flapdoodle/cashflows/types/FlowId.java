package de.flapdoodle.cashflows.types;

import org.immutables.value.Value;

@Value.Immutable
public interface FlowId<T> {
	String id();
	FlowType<T> type();

	@Value.Auxiliary
	default String asHumanReadable() {
		return id()+"("+type().type().getSimpleName()+")";
	}

	static <T> FlowId<T> of(String id, FlowType<T> type) {
		return ImmutableFlowId.<T>builder()        
			.id(id)
			.type(type)
			.build();
	}
}
