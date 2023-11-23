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

import java.time.LocalDateTime;
import java.util.function.BiFunction;

@Value.Immutable
public abstract class LocalDateTimeIterator implements LinearIterator<LocalDateTime> {

	@Value.Parameter
	protected abstract BiFunction<LocalDateTime, Integer, LocalDateTime> offset();

	@Override
	@Value.Auxiliary
	public LocalDateTime next(LocalDateTime start, int offset) {
		return offset().apply(start, offset);
	}

}
