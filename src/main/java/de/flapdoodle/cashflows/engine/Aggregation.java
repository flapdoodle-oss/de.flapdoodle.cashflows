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
package de.flapdoodle.cashflows.engine;

import de.flapdoodle.formula.Value;

import java.util.function.BiFunction;

@org.immutables.value.Value.Immutable
public interface Aggregation<T> {
	@org.immutables.value.Value.Parameter
	T start();

	@org.immutables.value.Value.Parameter
	Value<T> base();

	@org.immutables.value.Value.Parameter
	Value<T> delta();

	@org.immutables.value.Value.Parameter
	BiFunction<T, T, T> aggregate();

	static <T> Aggregation<T> of(T start, Value<T> base, Value<T> delta, BiFunction<T, T, T> aggregate) {
		return ImmutableAggregation.of(start, base, delta, aggregate);
	}
}
