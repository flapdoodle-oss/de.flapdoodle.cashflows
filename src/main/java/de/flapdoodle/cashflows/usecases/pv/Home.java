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
import de.flapdoodle.cashflows.usecases.types.KW;
import de.flapdoodle.cashflows.usecases.types.KWh;
import de.flapdoodle.formula.Value;
import de.flapdoodle.formula.calculate.Calculate;
import de.flapdoodle.formula.rules.Rules;
import de.flapdoodle.formula.values.Named;

public interface Home {
	default Named<Integer> dayOfTheYear() {
		return Value.named("dayOfTheYear", Integer.class);
	}

	default Named<KW> kwp() {
		return Value.named("pv KWp", KW.class);
	}

	default Named<KWh> pvPerKWp() {
		return Value.named("pvPerKWp", KWh.class);
	}

	default Named<KWh> pv() {
		return Value.named("pv", KWh.class);
	}

	default Named<KWh> summerConsumption() {
		return Value.named("summer consumption", KWh.class);
	}

	default Named<KWh> winterConsumption() {
		return Value.named("winter consumption", KWh.class);
	}

	default Named<KWh> consumption() {
		return Value.named("consumption", KWh.class);
	}

	default Rules rules() {
		return Rules.empty()
			.add(Calculate.value(pvPerKWp()).using(dayOfTheYear()).ifAllSetBy(Sun::pvPerKWp,"pvPerKWp(dayOfTheYear)"))
			.add(Calculate.value(pv()).using(pvPerKWp(), kwp()).ifAllSetBy((kWh, kw) -> kWh.multiply(kw.value()),"pvPerKWp*kwp"))
			.add(Calculate.value(consumption()).using(dayOfTheYear(), summerConsumption(), winterConsumption()).ifAllSetBy((dayOfYear, summer, winter) -> {
				double consumption = Ease.map(Ease.mirrorX(Ease::easeInOutCubic), dayOfYear / 365.0, summer.value(), winter.value());
				return KWh.of(consumption);
			},"consumption(dayOfTheYear,summer,winter)"));
	}
}
