package de.flapdoodle.cashflows.iterator;

import java.time.LocalDate;

public class LocalDateIterator implements LinearIterator<LocalDate> {

	@Override
	public LocalDate next(LocalDate start, int offset) {
		return start.plusDays(offset);
	}
}
