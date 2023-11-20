package de.flapdoodle.cashflows.iterator;

import org.immutables.value.Value;

import java.time.LocalDate;
import java.util.function.BiFunction;

@Value.Immutable
public abstract class LocalDateIterator implements LinearIterator<LocalDate> {

	@Value.Parameter
	protected abstract BiFunction<LocalDate, Integer, LocalDate> offset();

	@Override
	@Value.Auxiliary
	public LocalDate next(LocalDate start, int offset) {
		return offset().apply(start, offset);
	}
}
