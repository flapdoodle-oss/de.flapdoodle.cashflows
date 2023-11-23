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

import de.flapdoodle.cashflows.types.Id;
import de.flapdoodle.cashflows.usecases.types.KW;
import de.flapdoodle.cashflows.usecases.types.KWh;
import de.flapdoodle.formula.Value;
import de.flapdoodle.formula.calculate.Calculate;
import de.flapdoodle.formula.rules.Rules;
import de.flapdoodle.formula.values.Related;
import de.flapdoodle.reflection.TypeInfo;

@org.immutables.value.Value.Immutable
public abstract class PV implements Part {

    @org.immutables.value.Value.Default
    protected Id<PV> id() {
        return Id.of(TypeInfo.of(PV.class));
    }

    protected abstract KW kwpValue();

    @org.immutables.value.Value.Default
    public Related<KW, Id<PV>> kwp() {
        return Value.named("KWp", KW.class).relatedTo(id());
    }

    @org.immutables.value.Value.Default
    public Related<KWh, Id<PV>> energy() {
        return Value.named("energy", KWh.class).relatedTo(id());
    }

    @Override
    public Rules rules(Rules source) {
        return source
                .add(Calculate.value(kwp()).by(this::kwpValue,"KWp"))
                .add(Calculate.value(energy()).using(Location.pvPerKWp(), kwp())
                        .ifAllSetBy((kWh, kw) -> kWh.multiply(kw.value()),"pvPerKWp*kwp"))
                ;
    }

    public static ImmutablePV.Builder builder() {
        return ImmutablePV.builder();
    }
}
