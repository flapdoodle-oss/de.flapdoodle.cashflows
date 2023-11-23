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

import de.flapdoodle.checks.Preconditions;
import de.flapdoodle.reflection.TypeInfo;
import org.immutables.value.Value;

@Value.Immutable
public interface Iteration<T> {
	@Value.Parameter
	T last();

	@Value.Parameter
	T current();

	static <T> Iteration<T> of(T last, T current) {
		return ImmutableIteration.of(last, current);
	}

	@Value.Immutable
	abstract class IterationTypeInfo<T> implements TypeInfo<Iteration<T>> {
		@Value.Parameter
		protected abstract TypeInfo<T> type();

		@Override
		public Iteration<T> cast(Object instance) {
			Preconditions.checkArgument(isInstance(instance),"");
			return (Iteration<T>) instance;
		}

		@Override
		public boolean isInstance(Object instance) {
			return instance instanceof Iteration
				&& type().isInstance(((Iteration<?>) instance).last())
				&& type().isInstance(((Iteration<?>) instance).current());
		}
	}

	static <T> TypeInfo<Iteration<T>> typeInfo(TypeInfo<T> type) {
		return ImmutableIterationTypeInfo.of(type);
	}
}
