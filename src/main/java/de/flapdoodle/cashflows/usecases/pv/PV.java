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
