package de.flapdoodle.cashflows.iterator;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;

public abstract class ForwardIterators {

	public static ForwardIterator<LocalDate> EACH_DAY = last -> last.plusDays(1);
	public static ForwardIterator<LocalDateTime> EACH_HOUR = last -> last.plusHours(1);

	public static LocalDate nextWeekday(LocalDate now, DayOfWeek dayOfWeek) {
		DayOfWeek thisDayOfWeek = now.getDayOfWeek();
		int diff = thisDayOfWeek.getValue() - DayOfWeek.MONDAY.getValue();
		return now.plusDays(7 - diff);
	}
}
