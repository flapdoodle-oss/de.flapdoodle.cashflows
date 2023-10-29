package de.flapdoodle.cashflows.usescases.types;

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

	@Value.Auxiliary
	public KWh multiply(double factor) {
		return KWh.of(value() * factor);
	}

	@Value.Auxiliary
	public KWh divide(double divisor) {
		return KWh.of(value() / divisor);
	}

	public static KWh of(double value) {
		return ImmutableKWh.of(value);
	}
}
