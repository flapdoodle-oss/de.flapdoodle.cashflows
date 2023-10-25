package de.flapdoodle.cashflows.usecases;

import org.immutables.value.Value;

@Value.Immutable
public abstract class KWh {
	@Value.Parameter
	public abstract double value();

	public static KWh of(double value) {
		return ImmutableKWh.of(value);
	}

	public static KWh reduce(KWh a, KWh b) {
		return of(a.value() + b.value());
	}
}
