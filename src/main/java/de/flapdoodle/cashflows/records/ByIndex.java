package de.flapdoodle.cashflows.records;

import de.flapdoodle.cashflows.types.Change;
import de.flapdoodle.cashflows.types.FlowType;
import org.immutables.value.Value;

import java.util.List;
import java.util.Optional;

@Value.Immutable
public abstract class ByIndex<T> {
	@Value.Parameter
	protected abstract FlowType<T> type();
	public abstract List<Change<T>> changes();

	@Value.Lazy
	public Optional<T> aggregatedDelta() {
		return changes().stream()
			.map(Change::delta)
			.reduce(type().plus()::apply);
	}

	public ByIndex<T> add(Change<T> record) {
		return ImmutableByIndex.<T>builder()
			.from(this)
			.addChanges(record)
			.build();
	}

	public static <T> ByIndex<T> of(FlowType<T> type) {
		return ImmutableByIndex.of(type);
	}
}
