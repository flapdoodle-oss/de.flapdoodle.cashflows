package de.flapdoodle.cashflows.types;

import org.immutables.value.Value;

import java.util.function.BiFunction;

@Value.Immutable
public abstract class FlowType<T> {
	@Value.Parameter
	public abstract Class<T> type();

	@Value.Parameter
	public abstract BiFunction<T, T, T> plus();

	@Value.Parameter
	public abstract BiFunction<T, T, T> minus();

	public static final FlowType<Double> DOUBLE = of(Double.class, Double::sum, (a, b) -> a - b);
	public static final FlowType<Integer> INT = of(Integer.class, Integer::sum, (a, b) -> a - b);

	public static <T> FlowType<T> of(Class<T> type, BiFunction<T, T, T> plus, BiFunction<T, T, T> minus) {
		return ImmutableFlowType.of(type, plus, minus);
	}
}
