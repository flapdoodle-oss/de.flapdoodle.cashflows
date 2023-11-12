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
