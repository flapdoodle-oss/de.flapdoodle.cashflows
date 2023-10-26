package de.flapdoodle.cashflows.usecases;

import org.immutables.value.Value;

@Value.Immutable
public abstract class KWh {
	@Value.Parameter
	public abstract double value();

	@Value.Auxiliary
	public KWh minus(KWh other) {
		return KWh.of(value() - other.value());
	}

	@Value.Auxiliary
	public KWh plus(KWh other) {
		return KWh.of(value() + other.value());
	}

	public static KWh of(double value) {
		return ImmutableKWh.of(value);
	}
}
