package de.flapdoodle.cashflows.types;

import de.flapdoodle.cashflows.iterator.ForwardIterator;
import org.immutables.value.Value;

import java.util.Optional;

@Value.Immutable
public interface Range<T extends Comparable<? super T>> {
	T start();

	Optional<T> end();

	ForwardIterator<T> iterator();

	@Value.Auxiliary
	default boolean isActive(T current) {
		return start().compareTo(current)<=0  && (!end().isPresent() || current.compareTo(end().get()) <= 0);
	}

	@Value.Auxiliary
	default Range<T> nextRange() {
		return ImmutableRange.<T>builder()
			.start(iterator().next(start()))
			.end(end())
			.iterator(iterator())
			.build();
	}

	static <T extends Comparable<? super T>> Range<T> of(T start, ForwardIterator<T> next) {
		return ImmutableRange.<T>builder()
			.start(start)
			.iterator(next)
			.build();
	}

	static <T extends Comparable<? super T>> Range<T> of(T start, T end, ForwardIterator<T> next) {
		return ImmutableRange.<T>builder()
			.start(start)
			.iterator(next)
			.end(end)
			.build();
	}

}
