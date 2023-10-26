package de.flapdoodle.cashflows.iterator;

import java.time.LocalDate;
import java.time.LocalDateTime;

public abstract class LinearIterators {
	private LinearIterators() {
		// no instance
	}

	public static LinearIterator<LocalDate> EACH_DAY = new LocalDateIterator();
	public static LinearIterator<LocalDateTime> EACH_HOUR = ImmutableLocalDateTimeIterator.of(LocalDateTime::plusHours);
}
