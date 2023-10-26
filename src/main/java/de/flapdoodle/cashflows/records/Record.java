package de.flapdoodle.cashflows.records;

import de.flapdoodle.cashflows.types.Change;
import de.flapdoodle.cashflows.types.FlowId;
import org.immutables.value.Value;

@Value.Immutable
public abstract class Record<T> {
	public abstract FlowId<T> id();
	public abstract int offset();
	public abstract Change<T> change();

	public static <T> Record<T> of(FlowId<T> id, int index, Change<T> change) {
		return ImmutableRecord.<T>builder()
			.id(id)
			.offset(index)
			.change(change)
			.build();
	}
}
