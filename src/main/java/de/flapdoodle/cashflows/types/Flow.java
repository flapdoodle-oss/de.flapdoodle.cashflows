package de.flapdoodle.cashflows.types;

import org.immutables.value.Value;

@Value.Immutable
public interface Flow<T> {
	FlowId<T> id();

	T start();

	static <T> Flow<T> of(FlowId<T> id, T start) {
		return ImmutableFlow.<T>builder()
			.id(id)
			.start(start)
			.build();
	}
}
