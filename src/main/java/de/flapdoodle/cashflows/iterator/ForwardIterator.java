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
package de.flapdoodle.cashflows.iterator;

import org.immutables.value.Value;

import java.util.function.Function;

public interface ForwardIterator<T> {
	T next(T last);

	@Value.Auxiliary
	default ForwardIterator<T> andThen(Function<T, T> change) {
		ForwardIterator<T> that = this;

		return new ForwardIterator<T>() {
			@Override
			public T next(T last) {
				return change.apply(that.next(last));
			}

			@Override
			public String toString() {
				return that + " and then " + change;
			}
		};
	}
}
