package de.flapdoodle.cashflows.iterator;

import java.time.LocalDate;
import java.time.LocalDateTime;

public abstract class LinearIterators {
	private LinearIterators() {
		// no instance
	}

	public static LinearIterator<LocalDate> EACH_DAY = ImmutableLocalDateIterator.of(LocalDate::plusDays);
	public static LinearIterator<LocalDate> EACH_WEEK = ImmutableLocalDateIterator.of(LocalDate::plusWeeks);
	public static LinearIterator<LocalDateTime> EACH_HOUR = ImmutableLocalDateTimeIterator.of(LocalDateTime::plusHours);

	public static LinearIterator<LocalDate> eachNDay(int day) {
		return ImmutableLocalDateIterator.of((localDate, offset) -> localDate.plusDays(offset*day));
	}
}
