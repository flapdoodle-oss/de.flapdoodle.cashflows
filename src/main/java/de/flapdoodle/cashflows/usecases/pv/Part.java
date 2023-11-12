package de.flapdoodle.cashflows.usecases.pv;

import de.flapdoodle.formula.rules.Rules;
import org.immutables.value.Value;

public interface Part {
    @Value.Lazy
    Rules rules();
}
