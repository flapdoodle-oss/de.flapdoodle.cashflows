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
package de.flapdoodle.cashflows.types;

import de.flapdoodle.cashflows.iterator.ForwardIterator;
import org.immutables.value.Value;

import java.util.Optional;

@Value.Immutable
public interface Range<T extends Comparable<? super T>> {
	T start();

	Optional<T> end();

	ForwardIterator<T> iterator();

	@Value.Auxiliary
	default boolean isActive(T current) {
		return start().compareTo(current)<=0  && (!end().isPresent() || current.compareTo(end().get()) <= 0);
	}

	@Value.Auxiliary
	default Range<T> nextRange() {
		return ImmutableRange.<T>builder()
			.start(iterator().next(start()))
			.end(end())
			.iterator(iterator())
			.build();
	}

	static <T extends Comparable<? super T>> Range<T> of(T start, ForwardIterator<T> next) {
		return ImmutableRange.<T>builder()
			.start(start)
			.iterator(next)
			.build();
	}

	static <T extends Comparable<? super T>> Range<T> of(T start, T end, ForwardIterator<T> next) {
		return ImmutableRange.<T>builder()
			.start(start)
			.iterator(next)
			.end(end)
			.build();
	}

}
