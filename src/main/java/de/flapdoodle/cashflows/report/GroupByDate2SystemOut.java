package de.flapdoodle.cashflows.report;

import de.flapdoodle.cashflows.records.ByFlowId;
import de.flapdoodle.cashflows.records.ByIndex;
import de.flapdoodle.cashflows.records.Records;
import de.flapdoodle.cashflows.types.Change;
import de.flapdoodle.cashflows.types.FlowId;

import java.util.Collection;

public class GroupByDate2SystemOut<T> implements RecordsRenderer<T> {

	@Override
	public void render(Records<T> records, Collection<? extends FlowId<?>> flowIds) {
		for (Integer offset : records.offsets()) {
			System.out.println(records.mapOffset(offset));
			for (FlowId<?> id : flowIds) {
				System.out.println("  "+id.asHumanReadable());
				ByFlowId<?> byFlowId = records.get(id);
				ByIndex<?> byIndex = byFlowId.get(offset);
				for (Change<?> change : byIndex.changes()) {
					System.out.println("    " + change.name() + ": " + change.delta());
				}
				System.out.println("    => " + byFlowId.stateOf(offset).after());
			}
		}
	}
}
