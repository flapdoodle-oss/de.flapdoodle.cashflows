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

import java.time.LocalDate;
import java.util.function.BiFunction;

@Value.Immutable
public abstract class LocalDateIterator implements LinearIterator<LocalDate> {

	@Value.Parameter
	protected abstract BiFunction<LocalDate, Integer, LocalDate> offset();

	@Override
	@Value.Auxiliary
	public LocalDate next(LocalDate start, int offset) {
		return offset().apply(start, offset);
	}
}
