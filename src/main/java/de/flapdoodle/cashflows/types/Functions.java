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

import java.util.function.Function;

public abstract class Functions {
	private Functions() {
		// no instance
	}

	public static <S, T>Function<S, T> withDescription(Function<S, T> delegate, String description) {
		return new Function<S, T>() {
			@Override
			public T apply(S s) {
				return delegate.apply(s);
			}

			@Override
			public String toString() {
				return description;
			}
		};
	}
}
