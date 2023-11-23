/*
 * Copyright (C) 2022
 *   Michael Mosmann <michael@mosmann.de>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
