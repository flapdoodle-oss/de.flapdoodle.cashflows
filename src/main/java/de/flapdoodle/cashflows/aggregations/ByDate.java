package de.flapdoodle.cashflows.aggregations;

import de.flapdoodle.cashflows.types.Change;
import org.immutables.value.Value;

import java.util.List;

@Value.Immutable
public abstract class ByDate<T> {
	protected abstract List<Change<T>> changes();

	public ByDate<T> add(Change<T> record) {
		return ImmutableByDate.<T>builder()
			.from(this)
			.addChanges(record)
			.build();
	}

	public static <T> ByDate<T> empty() {
		return ImmutableByDate.<T>builder()
			.build();
	}
}
