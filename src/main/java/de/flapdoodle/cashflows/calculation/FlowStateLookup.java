package de.flapdoodle.cashflows.calculation;

import de.flapdoodle.cashflows.types.FlowId;
import de.flapdoodle.cashflows.types.FlowState;

public interface FlowStateLookup {
	<T> FlowState<T> stateOf(FlowId<T> id);
}
