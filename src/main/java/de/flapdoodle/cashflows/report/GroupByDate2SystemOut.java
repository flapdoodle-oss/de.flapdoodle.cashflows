package de.flapdoodle.cashflows.report;

import de.flapdoodle.cashflows.aggregations.ByDate;
import de.flapdoodle.cashflows.aggregations.ByFlowId;
import de.flapdoodle.cashflows.aggregations.FlowRecords;
import de.flapdoodle.cashflows.types.Change;
import de.flapdoodle.cashflows.types.FlowId;

import java.time.LocalDate;
import java.util.Collection;

public class GroupByDate2SystemOut implements RecordRenderer {

	@Override
	public void render(FlowRecords records, Collection<? extends FlowId<?>> flowIds) {
		for (LocalDate date : records.dates()) {
			System.out.println(date);
			for (FlowId<?> id : flowIds) {
				System.out.println("  "+id.id() + ": " + id.type().type());
				ByFlowId<?> byFlowId = records.get(id);
				ByDate<?> byDate = byFlowId.get(date);
				for (Change<?> change : byDate.changes()) {
					System.out.println("    " + change.name() + ": " + change.delta());
				}
			}
		}
	}
}
