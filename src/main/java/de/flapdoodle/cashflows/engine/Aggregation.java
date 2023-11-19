package de.flapdoodle.cashflows.engine;

import de.flapdoodle.formula.Value;

import java.util.function.BiFunction;

@org.immutables.value.Value.Immutable
public interface Aggregation<T> {
	@org.immutables.value.Value.Parameter
	T start();

	@org.immutables.value.Value.Parameter
	Value<T> base();

	@org.immutables.value.Value.Parameter
	Value<T> delta();

	@org.immutables.value.Value.Parameter
	BiFunction<T, T, T> aggregate();

	static <T> Aggregation<T> of(T start, Value<T> base, Value<T> delta, BiFunction<T, T, T> aggregate) {
		return ImmutableAggregation.of(start, base, delta, aggregate);
	}
}
