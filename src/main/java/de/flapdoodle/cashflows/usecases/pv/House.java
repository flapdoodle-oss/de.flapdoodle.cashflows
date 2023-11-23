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
import de.flapdoodle.formula.Value;
import de.flapdoodle.formula.calculate.Calculate;
import de.flapdoodle.formula.rules.Rules;
import de.flapdoodle.formula.values.Named;

import java.util.Optional;

@org.immutables.value.Value.Immutable
public abstract class House implements Part {

    public abstract Optional<PV> pv();

    public abstract Location location();

    public abstract KWh summerConsumption();

    public abstract KWh winterConsumption();

    @org.immutables.value.Value.Default
    public Named<KWh> consumption() {
        return Value.named("consumption", KWh.class);
    }

    public Named<KWh> fromGrid() {
        return Value.named("fromGrid", KWh.class);
    }

    public Named<KWh> toGrid() {
        return Value.named("toGrid", KWh.class);
    }

    @Override
    @org.immutables.value.Value.Auxiliary
    public Rules rules(Rules src) {
        return location().rules(localRules(src));
    }

    @org.immutables.value.Value.Auxiliary
    protected Rules localRules(Rules src) {
        Rules ret = src
                .add(Calculate.value(consumption())
                        .requiring(Location.dayOfTheYear()).by((dayOfYear) -> consumption(dayOfYear, summerConsumption(), winterConsumption()), "consumption(dayOfTheYear)"));
        if (pv().isPresent()) {
            PV pv = pv().get();
            ret= pv.rules(ret)
                    .add(Calculate.value(toGrid()).requiring(consumption(), pv.energy()).by((needed, fromPV) -> {
                        KWh toGrid = fromPV.minus(needed);
                        return toGrid.value() < 0
                          ? KWh.of(0)
                          : toGrid;
                    },"consumption-pv"));
        } else {
            ret = ret.add(Calculate.value(toGrid()).by(() -> KWh.of(0)));
        }
        return ret;
    }

    public static KWh consumption(int dayOfYear, KWh summerConsumption, KWh winterConsumption) {
        return KWh.of(Ease.map(Ease.flipY(Ease.mirrorX(Ease::easeInOutCubic)), dayOfYear / 365.0, summerConsumption.value(), winterConsumption.value()));
    }

    public static ImmutableHouse.Builder builder() {
        return ImmutableHouse.builder();
    }
}
