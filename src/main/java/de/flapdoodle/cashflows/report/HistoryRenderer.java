package de.flapdoodle.cashflows.report;

import de.flapdoodle.cashflows.records.History;
import de.flapdoodle.formula.Value;

import java.util.Collection;

public interface HistoryRenderer<T, R> {
	R render(History<T> records, Collection<? extends Value<?>> ids);
}
