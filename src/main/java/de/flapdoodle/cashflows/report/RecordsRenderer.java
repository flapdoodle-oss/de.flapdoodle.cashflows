package de.flapdoodle.cashflows.report;

import de.flapdoodle.cashflows.records.Records;
import de.flapdoodle.cashflows.types.FlowId;

import java.util.Collection;

public interface RecordsRenderer<T> {
	void render(Records<T> records, Collection<? extends FlowId<?>> flowIds);
}
