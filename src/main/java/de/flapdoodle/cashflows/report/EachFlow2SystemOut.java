package de.flapdoodle.cashflows.report;

import de.flapdoodle.cashflows.aggregations.ByDate;
import de.flapdoodle.cashflows.aggregations.ByFlowId;
import de.flapdoodle.cashflows.aggregations.FlowRecords;
import de.flapdoodle.cashflows.types.Change;
import de.flapdoodle.cashflows.types.FlowId;

import java.time.LocalDate;
import java.util.Collection;

public class EachFlow2SystemOut implements RecordRenderer {
	@Override
	public void render(FlowRecords records, Collection<? extends FlowId<?>> flowIds) {
		for (FlowId<?> id : flowIds) {
			System.out.println(id.id() + ": " + id.type().type());
			ByFlowId<?> byFlowId = records.get(id);
			for (LocalDate date : byFlowId.dates()) {
				System.out.println("  " + date);
				ByDate<?> byDate = byFlowId.get(date);
				for (Change<?> change : byDate.changes()) {
					System.out.println("    " + change.name() + ": " + change.delta());
				}
			}
		}
	}
}
