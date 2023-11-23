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
package de.flapdoodle.cashflows.usecases.pv;

import de.flapdoodle.cashflows.generators.Ease;
import de.flapdoodle.cashflows.usecases.types.KWh;

public abstract class Sun {

	public static double normal(int dayOfTheYear, double winter, double summer) {
		double factor = Ease.sinusUpDown(dayOfTheYear / 365.0);
		return winter + (summer - winter) * factor;
	}

	public static double dayLength(int dayOfTheYear) {
		return normal(dayOfTheYear, 7.5, 17);
	}

	public static double sunshineHours(int dayOfTheYear) {
		return normal(dayOfTheYear, 35.0/30, 300/30.0);
	}

	public static double cloudyHours(int dayOfTheYear) {
		return dayLength(dayOfTheYear) - sunshineHours(dayOfTheYear);
	}

	// Beispiele aus dem Netz
	// 10kWp -> 1200kWh pro Monat - Sommer
	// 10kWp -> 837kWh pro Monat - Winter
	public static KWh pvPerKWp(int dayOfYear) {
		double maxkWhproKW = 110.0 / 30.0;
		double minkWhproKW = 8.37 / 30.0;
		return KWh.of(Sun.normal(dayOfYear, minkWhproKW, maxkWhproKW));
	}

	public static KWh pvPerKWpPerSunshineHour(int dayOfYear) {
		return pvPerKWp(dayOfYear).divide(sunshineHours(dayOfYear));
	}

}
