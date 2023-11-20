package de.flapdoodle.cashflows.report;

import de.flapdoodle.cashflows.records.History;
import de.flapdoodle.formula.Value;
import de.flapdoodle.formula.types.HasHumanReadableLabel;

import java.util.Collection;
import java.util.function.Function;

public class History2Csv<I> implements HistoryRenderer<I, String> {

	private final Function<Object, String> toCSV;
//
	public History2Csv(Function<Object, String> toCSV) {
		this.toCSV = toCSV;
	}

	@Override
	public String render(History<I> history, Collection<? extends Value<?>> ids) {
		StringBuilder sb=new StringBuilder();

		sb.append("Date;");
		ids.forEach(id -> {
			String asLabel = HasHumanReadableLabel.asHumanReadable(id);
			sb.append("\"").append(asLabel).append("\";");
		});
		sb.append("\n");

		for (Integer offset : history.offsets()) {
			sb.append(toCSV.apply(history.indexOf(offset)));
			sb.append(";");
			ids.forEach(id -> {
				sb.append(toCSV.apply(history.get(offset, id)));
				sb.append(";");
			});

			sb.append("\n");
		}

		return sb.toString();
	}

//	@Override
//	public void render(Records<LocalDate> records, Collection<? extends FlowId<?>> flowIds) {
//		sb.append("Date;");
//		flowIds.forEach(id -> {
//			sb.append(id.id());
//			sb.append(";");
//		});
//		sb.append("\n");
//
//		for (Integer offset : records.offsets()) {
//			sb.append(toCSV.apply(records.mapOffset(offset)));
//			sb.append(";");
//			for (FlowId<?> id : flowIds) {
////				System.out.print("  "+id.asHumanReadable());
//				ByFlowId<?> byFlowId = records.get(id);
//				ByIndex<?> byIndex = byFlowId.get(offset);
////				for (Change<?> change : byIndex.changes()) {
////					System.out.println("    " + change.name() + ": " + change.delta());
////				}
//				FlowState flowState = byFlowId.stateOf(offset);
//				BiFunction minus = id.type().minus();
////				sb.append(toCSV.apply(flowState.after()));
////				sb.append(";");
//				sb.append(toCSV.apply(minus.apply(flowState.after(), flowState.before())));
//				sb.append(";");
//			}
//			sb.append("\n");
//		}
//	}
//
//	public String renderResult() {
//		return sb.toString();
//	}
}
