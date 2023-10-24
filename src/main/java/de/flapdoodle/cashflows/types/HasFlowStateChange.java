package de.flapdoodle.cashflows.types;

public interface HasFlowStateChange<T> {
	T before();
	T after();
}
