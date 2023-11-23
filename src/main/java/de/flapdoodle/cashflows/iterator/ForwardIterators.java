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

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;

public abstract class ForwardIterators {

	public static ForwardIterator<LocalDate> EACH_DAY = last -> last.plusDays(1);
	public static ForwardIterator<LocalDateTime> EACH_HOUR = last -> last.plusHours(1);

	public static LocalDate nextWeekday(LocalDate now, DayOfWeek dayOfWeek) {
		DayOfWeek thisDayOfWeek = now.getDayOfWeek();
		int diff = thisDayOfWeek.getValue() - DayOfWeek.MONDAY.getValue();
		return now.plusDays(7 - diff);
	}
}
