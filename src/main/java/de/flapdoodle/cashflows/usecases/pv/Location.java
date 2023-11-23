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

import de.flapdoodle.cashflows.usecases.types.KWh;
import de.flapdoodle.formula.Value;
import de.flapdoodle.formula.calculate.Calculate;
import de.flapdoodle.formula.rules.Rules;
import de.flapdoodle.formula.values.Related;

import java.time.LocalDate;

@org.immutables.value.Value.Immutable
public abstract class Location implements Part {
    @org.immutables.value.Value.Default
    protected LocalDate localDateValue() {
        return LocalDate.now();
    }

    @Override
    public Rules rules(Rules source) {
        return source
                .add(Calculate.value(localDate()).by(this::localDateValue,"localDate"))
                .add(Calculate.value(dayOfTheYear()).requiring(localDate())
                        .by(LocalDate::getDayOfYear,"dayOfTheYear"))
                .add(Calculate.value(pvPerKWp()).requiring(dayOfTheYear())
                        .by(Sun::pvPerKWp,"pvPerKWp(dayOfTheYear)"));
    }

    public static ImmutableLocation.Builder builder() {
        return ImmutableLocation.builder();
    }

    public static Related<LocalDate, Class<Location>> localDate() {
        return Value.ofType(LocalDate.class).relatedTo(Location.class);
    }

    public static Related<KWh, Class<Location>> pvPerKWp() {
        return Value.named("pvPerKWp", KWh.class).relatedTo(Location.class);
    }

    public static Related<Integer, Class<Location>> dayOfTheYear() {
        return Value.named("dayOfTheYear", Integer.class).relatedTo(Location.class);
    }
}
