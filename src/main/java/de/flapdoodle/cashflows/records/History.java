package de.flapdoodle.cashflows.records;

import de.flapdoodle.cashflows.iterator.LinearIterator;
import de.flapdoodle.checks.Preconditions;
import org.immutables.value.Value;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Value.Immutable
public abstract class History<I> {
	protected abstract I start();
	protected abstract LinearIterator<I> iterator();
	protected abstract Map<Integer, Entry> entries();

	@Value.Auxiliary
	public <T> T get(int offset, de.flapdoodle.formula.Value<T> id) {
		Entry entry = Preconditions.checkNotNull(entries().get(offset), "no entry for %s", offset);
		return entry.get(id);
	}

	@Value.Lazy
	public List<Integer> offsets() {
		return entries().keySet().stream()
			.sorted()
			.collect(Collectors.toList());
	}

	@Value.Auxiliary
	public I indexOf(Integer offset) {
		return iterator().next(start(), offset);
	}

	@Value.Auxiliary
	public History<I> change(int offset, Consumer<ChangeListener> onChange) {
		ImmutableEntry.Builder builder = ImmutableEntry.builder();
		onChange.accept(new ChangeListener() {
			@Override
			public <T> void set(de.flapdoodle.formula.Value<T> id, T value) {
				builder.putMap(id, value);
			}
			@Override
			public <T> T last(de.flapdoodle.formula.Value<T> id) {
				return get(offset-1, id);
			}
		});

		return ImmutableHistory.<I>builder()
			.from(this)
			.putEntries(offset, builder.build())
			.build();
	}

	public interface ChangeListener {
		<T> void set(de.flapdoodle.formula.Value<T> id, T value);

		<T> T last(de.flapdoodle.formula.Value<T> id);
	}

	@Value.Immutable
	public static abstract class Entry {
		protected abstract Map<de.flapdoodle.formula.Value<?>, Object> map();

		@Value.Auxiliary
		public <T> T get(de.flapdoodle.formula.Value<T> id) {
			return Preconditions.checkNotNull((T) map().get(id),"not implemented");
		}
	}

	public static <T> History<T> with(LinearIterator<T> iterator, T start) {
		return ImmutableHistory.<T>builder()
			.iterator(iterator)
			.start(start)
			.build();
	}
}
