package de.flapdoodle.cashflows.tests;

import org.immutables.value.Value;

@Value.Immutable
public interface Position {
	@Value.Parameter
	double x();

	@Value.Parameter
	double y();

	static Position of(double x, double y) {
		return ImmutablePosition.of(x, y);
	}
}
