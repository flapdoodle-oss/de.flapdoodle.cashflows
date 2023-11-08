package de.flapdoodle.cashflows.usecases.types;

import org.immutables.value.Value;

@Value.Immutable
public abstract class KW {
	@Value.Parameter
	public abstract double value();

	public static KW of(double value) {
		return ImmutableKW.of(value);
	}

	public static KW reduce(KW a, KW b) {
		return of(a.value() + b.value());
	}
}
