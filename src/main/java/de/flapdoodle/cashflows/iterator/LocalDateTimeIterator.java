package de.flapdoodle.cashflows.iterator;

import org.immutables.value.Value;

import java.time.LocalDateTime;
import java.util.function.BiFunction;

@Value.Immutable
public abstract class LocalDateTimeIterator implements LinearIterator<LocalDateTime> {

	@Value.Parameter
	protected abstract BiFunction<LocalDateTime, Integer, LocalDateTime> offset();

	@Override
	@Value.Auxiliary
	public LocalDateTime next(LocalDateTime start, int offset) {
		return offset().apply(start, offset);
	}

}
