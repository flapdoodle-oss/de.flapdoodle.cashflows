package de.flapdoodle.cashflows.types;

import org.immutables.value.Value;

@Value.Immutable
public interface Change<T> {
	String name();
	T delta();

	static <T> Change<T> of(String name, T delta) {
		return ImmutableChange.<T>builder()
			.name(name)
			.delta(delta)
			.build();
	}
}
