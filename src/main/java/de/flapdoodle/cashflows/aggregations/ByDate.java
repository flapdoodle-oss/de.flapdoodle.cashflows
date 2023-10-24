package de.flapdoodle.cashflows.aggregations;

import de.flapdoodle.cashflows.types.Change;
import de.flapdoodle.cashflows.types.FlowType;
import org.immutables.value.Value;

import java.util.List;
import java.util.Optional;

@Value.Immutable
public abstract class ByDate<T> {
	@Value.Parameter
	protected abstract FlowType<T> type();
	public abstract List<Change<T>> changes();

	@Value.Lazy
	public Optional<T> aggregatedDelta() {
		return changes().stream()
			.map(Change::delta)
			.reduce(type().reduce()::apply);
	}

	public ByDate<T> add(Change<T> record) {
		return ImmutableByDate.<T>builder()
			.from(this)
			.addChanges(record)
			.build();
	}

	public static <T> ByDate<T> of(FlowType<T> type) {
		return ImmutableByDate.of(type);
	}
}
