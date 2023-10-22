package de.flapdoodle.cashflows.engine;

import de.flapdoodle.cashflows.calculation.FlowStateLookup;
import de.flapdoodle.cashflows.types.FlowId;
import de.flapdoodle.cashflows.types.FlowState;
import de.flapdoodle.checks.Preconditions;
import org.immutables.value.Value;

import java.time.LocalDate;
import java.util.Map;

@Value.Immutable
public abstract class FlowStates {
	protected abstract Map<LocalDate, FlowStateMap> values();

	protected <T> FlowState<T> aggregate(FlowId<T> id,LocalDate start, LocalDate end) {
		FlowStateMap startStateMap = get(start);
		FlowState<T> startState = Preconditions.checkPresent(startStateMap.get(id),"could not find state for %s", id).get();
		T s = startState.before();

		FlowStateMap endStateMap = get(end);
		FlowState<T> endState = Preconditions.checkPresent(endStateMap.get(id),"could not find state for %s", id).get();
		T e = endState.after();

		return FlowState.of(s,e);
	}

	@Value.Auxiliary
	public FlowStateLookup stateLookupOf(LocalDate lastDate, LocalDate current) {
		return new FlowStateLookup() {
			@Override
			public <T> FlowState<T> stateOf(FlowId<T> id) {
				return aggregate(id, lastDate, current);
			}
		};
	}

	public FlowStateMap get(LocalDate localDate) {
		return Preconditions.checkNotNull(values().get(localDate),"could not get entry for %s", localDate);
	}

	public FlowStates with(LocalDate date, FlowStateMap states) {
		return ImmutableFlowStates.builder().from(this)
			.putValues(date, states)
			.build();
	}

	public static FlowStates empty() {
		return ImmutableFlowStates.builder()
			.build();
	}
}
