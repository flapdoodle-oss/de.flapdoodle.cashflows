package de.flapdoodle.cashflows.types;

import org.immutables.value.Value;

import java.time.LocalDate;

@Value.Immutable
public abstract class FlowRecord<T> {
	public abstract FlowId<T> id();
	public abstract LocalDate date();
	public abstract Change<T> change();

	public static <T> FlowRecord<T> of(FlowId<T> id, LocalDate date, Change<T> change) {
		return ImmutableFlowRecord.<T>builder()
			.id(id)
			.date(date)
			.change(change)
			.build();
	}
}
