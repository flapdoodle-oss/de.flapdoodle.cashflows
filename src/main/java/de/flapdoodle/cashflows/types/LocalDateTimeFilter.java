package de.flapdoodle.cashflows.types;

import java.time.LocalDateTime;
import java.util.function.Function;

public abstract class LocalDateTimeFilter {
	private LocalDateTimeFilter() {
		// no instance
	}

	public static LocalDateTime withoutNanos(LocalDateTime value) {
		return value.withNano(0);
	}

	public static Function<LocalDateTime, LocalDateTime> withoutNanos() {
		return Functions.withDescription(LocalDateTimeFilter::withoutNanos,"withoutNanos");
	}
}
