package de.flapdoodle.cashflows.iterator;

import org.immutables.value.Value;

import java.util.function.Function;

public interface ForwardIterator<T> {
	T next(T last);

	@Value.Auxiliary
	default ForwardIterator<T> andThen(Function<T, T> change) {
		ForwardIterator<T> that = this;

		return new ForwardIterator<T>() {
			@Override
			public T next(T last) {
				return change.apply(that.next(last));
			}

			@Override
			public String toString() {
				return that + " and then " + change;
			}
		};
	}
}
