package de.flapdoodle.cashflows.types;

import org.immutables.value.Value;

import java.util.UUID;

@Value.Immutable
public interface FlowId<T> {
	String id();
	FlowType<T> type();

	static <T> FlowId<T> of(String id, FlowType<T> type) {
		return ImmutableFlowId.<T>builder()        
			.id(id)
			.type(type)
			.build();
	}
}
