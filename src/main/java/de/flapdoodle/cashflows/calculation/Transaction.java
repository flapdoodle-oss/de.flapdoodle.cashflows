package de.flapdoodle.cashflows.calculation;

import de.flapdoodle.cashflows.types.Range;
import org.immutables.value.Value;

import java.util.List;

@Value.Immutable
public interface Transaction<T extends Comparable<? super T>> {
	Range<T> section();
	List<Calculation<T, ?>> calculations();

	static <T extends Comparable<? super T>> ImmutableTransaction.Builder<T> builder() {
		return ImmutableTransaction.builder();
	}
}
