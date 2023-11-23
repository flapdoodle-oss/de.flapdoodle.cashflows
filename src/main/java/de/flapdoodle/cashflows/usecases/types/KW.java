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
package de.flapdoodle.cashflows.usecases.types;

import org.immutables.value.Value;

@Value.Immutable
public abstract class KW {
	@Value.Parameter
	public abstract double value();

	public static KW of(double value) {
		return ImmutableKW.of(value);
	}

	public static KW reduce(KW a, KW b) {
		return of(a.value() + b.value());
	}
}
