package de.flapdoodle.cashflows.types;

import de.flapdoodle.checks.Preconditions;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class Maps {

	public static <K, V> Map<K, V> changeEntry(Map<K, V> source, K key, Function<V, V> valueChange) {
		Preconditions.checkNotNull(source.containsKey(key), "key %s not found in %s", key, source);

		return source.entrySet().stream()
			.collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getKey().equals(key)
				? valueChange.apply(entry.getValue())
				: entry.getValue()));
	}
}
