package de.flapdoodle.cashflows.types;

import java.util.function.Function;

public abstract class Functions {
	private Functions() {
		// no instance
	}

	public static <S, T>Function<S, T> withDescription(Function<S, T> delegate, String description) {
		return new Function<S, T>() {
			@Override
			public T apply(S s) {
				return delegate.apply(s);
			}

			@Override
			public String toString() {
				return description;
			}
		};
	}
}
