package de.flapdoodle.cashflows.iterator;

import org.immutables.value.Value;

import java.util.function.Function;

public interface LinearIterator<T> {
	T next(T start, int offset);

	@Value.Auxiliary
	default LinearIterator<T> andThen(Function<T, T> change) {
		LinearIterator<T> that = this;

		return new LinearIterator<T>() {
			@Override
			public T next(T start, int offset) {
				return change.apply(that.next(start, offset));
			}

			@Override
			public String toString() {
				return that + " and then " + change;
			}
		};
	}
}
