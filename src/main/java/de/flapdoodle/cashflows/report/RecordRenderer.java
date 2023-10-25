package de.flapdoodle.cashflows.report;

import de.flapdoodle.cashflows.aggregations.FlowRecords;
import de.flapdoodle.cashflows.types.FlowId;

import java.util.Collection;

public interface RecordRenderer {
	void render(FlowRecords records, Collection<? extends FlowId<?>> flowIds);
}
