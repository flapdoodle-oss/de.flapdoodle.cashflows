package de.flapdoodle.cashflows.aggregations;

import de.flapdoodle.cashflows.types.Flow;
import de.flapdoodle.cashflows.types.FlowRecord;
import de.flapdoodle.cashflows.types.Maps;
import org.immutables.value.Value;

import java.time.LocalDate;
import java.util.Map;

@Value.Immutable
public abstract class ByFlowId<T> {
	@Value.Parameter
	public abstract Flow<T> flow();
	protected abstract Map<LocalDate, ByDate<T>> map();

	public ByFlowId<T> add(FlowRecord<T> record) {
		if (map().containsKey(record.date())) {
			return ImmutableByFlowId.<T>builder()
				.flow(flow())
				.map(Maps.changeEntry(map(), record.date(), byDate -> byDate.add(record.change())))
				.build();
		} else {
			return ImmutableByFlowId.<T>builder()
				.from(this)
				.putMap(record.date(), ByDate.<T>empty().add(record.change()))
				.build();
		}
	}

	public static <T> ByFlowId<T> of(Flow<T> flow) {
		return ImmutableByFlowId.of(flow);
	}
}
