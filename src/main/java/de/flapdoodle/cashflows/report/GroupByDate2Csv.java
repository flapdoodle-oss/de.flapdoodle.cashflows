package de.flapdoodle.cashflows.report;

import de.flapdoodle.cashflows.records.ByFlowId;
import de.flapdoodle.cashflows.records.ByIndex;
import de.flapdoodle.cashflows.records.Records;
import de.flapdoodle.cashflows.types.Change;
import de.flapdoodle.cashflows.types.FlowId;

import java.time.LocalDate;
import java.util.Collection;
import java.util.function.Function;

public class GroupByDate2Csv implements RecordsRenderer<LocalDate> {

	private final Function<Object, String> toCSV;
	private final StringBuilder sb=new StringBuilder();

	public GroupByDate2Csv(Function<Object, String> toCSV) {
		this.toCSV = toCSV;
	}

	@Override
	public void render(Records<LocalDate> records, Collection<? extends FlowId<?>> flowIds) {
		sb.append("Date;");
		flowIds.forEach(id -> {
			sb.append(id.id());
			sb.append(";");
		});
		sb.append("\n");

		for (Integer offset : records.offsets()) {
			sb.append(toCSV.apply(records.mapOffset(offset)));
			sb.append(";");
			for (FlowId<?> id : flowIds) {
//				System.out.print("  "+id.asHumanReadable());
				ByFlowId<?> byFlowId = records.get(id);
				ByIndex<?> byIndex = byFlowId.get(offset);
//				for (Change<?> change : byIndex.changes()) {
//					System.out.println("    " + change.name() + ": " + change.delta());
//				}
				sb.append(toCSV.apply(byFlowId.stateOf(offset).after()));
				sb.append(";");
			}
			sb.append("\n");
		}
	}

	public String renderResult() {
		return sb.toString();
	}
}
