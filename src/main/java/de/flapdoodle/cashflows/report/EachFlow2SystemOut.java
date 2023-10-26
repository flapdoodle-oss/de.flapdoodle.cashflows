package de.flapdoodle.cashflows.report;

import de.flapdoodle.cashflows.records.ByFlowId;
import de.flapdoodle.cashflows.records.ByIndex;
import de.flapdoodle.cashflows.records.Records;
import de.flapdoodle.cashflows.types.Change;
import de.flapdoodle.cashflows.types.FlowId;

import java.util.Collection;

public class EachFlow2SystemOut<T> implements RecordsRenderer<T> {
	@Override
	public void render(Records<T> records, Collection<? extends FlowId<?>> flowIds) {
		for (FlowId<?> id : flowIds) {
			System.out.println(id.asHumanReadable());
			ByFlowId<?> byFlowId = records.get(id);
			for (Integer offset : byFlowId.offsets()) {
				System.out.println("  " + records.mapOffset(offset));
				ByIndex<?> byDate = byFlowId.get(offset);
				for (Change<?> change : byDate.changes()) {
					System.out.println("    " + change.name() + ": " + change.delta());
				}
				System.out.println("    => " +byFlowId.stateOf(offset).after());
			}
		}
	}
}
