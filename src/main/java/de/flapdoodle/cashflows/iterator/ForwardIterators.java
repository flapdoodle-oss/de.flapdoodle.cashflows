package de.flapdoodle.cashflows.iterator;

import java.time.LocalDate;
import java.time.LocalDateTime;

public abstract class ForwardIterators {

	public static ForwardIterator<LocalDate> EACH_DAY = last -> last.plusDays(1);
	public static ForwardIterator<LocalDateTime> EACH_HOUR = last -> last.plusHours(1);
}
