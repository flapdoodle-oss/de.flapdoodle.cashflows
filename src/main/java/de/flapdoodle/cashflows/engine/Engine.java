package de.flapdoodle.cashflows.engine;

import de.flapdoodle.cashflows.aggregations.FlowRecords;
import de.flapdoodle.cashflows.calculation.Calculation;
import de.flapdoodle.cashflows.calculation.FlowStateLookup;
import de.flapdoodle.cashflows.calculation.Transaction;
import de.flapdoodle.cashflows.types.Change;
import de.flapdoodle.cashflows.types.Flow;
import de.flapdoodle.cashflows.types.FlowId;
import de.flapdoodle.cashflows.types.FlowRecord;
import de.flapdoodle.checks.Preconditions;
import org.immutables.value.Value;

import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.Function;

@Value.Immutable
public abstract class Engine {
	protected abstract List<Flow<?>> flows();
	protected abstract List<Transaction> transactions();

	@Value.Default
	protected Function<LocalDate, LocalDate> dateIterator() {
		return it -> it.plusDays(1);
	}

	public FlowRecords calculate(LocalDate start, LocalDate end) {
		Preconditions.checkArgument(start.isBefore(end),"%s >= %s", start, end);

		FlowRecords records = FlowRecords.of(flows());

		Map<Transaction, LocalDate> lastRun=new LinkedHashMap<>();
		LocalDate current=start;

		do {
			List<FlowChangeEntry<?>> changes=new ArrayList<>();

			for (Transaction transaction : transactions()) {
				if (transaction.section().isActive(current)) {
					LocalDate transactionLastRun = Optional.ofNullable(lastRun.get(transaction))
						.orElse(start);
					LocalDate latestResultsBeforeCurrent = current.minusDays(1);
					FlowStateLookup flowStateLookup = records.stateLookupOf(transactionLastRun, latestResultsBeforeCurrent);

					Duration duration = Duration.ofDays(ChronoUnit.DAYS.between(transactionLastRun, current));

					for (Calculation<?> calculation : transaction.calculations()) {
						FlowChangeEntry<?> entry = calculate(calculation, flowStateLookup, duration);
						changes.add(entry);
					}

					lastRun.put(transaction, current);
				}
			}

			records = merge(records, current, changes);

			current=dateIterator().apply(current);
		} while (!current.isAfter(end));

		return records;
	}

	private FlowRecords merge(FlowRecords records, LocalDate now, List<FlowChangeEntry<?>> changes) {
		FlowRecords current = records;
		for (FlowChangeEntry<?> it : changes) {
			current=current.add(asRecord(now, it));
		}
		return current;
	}

	private static <T> FlowRecord<T> asRecord(LocalDate now, FlowChangeEntry<T> it) {
		return FlowRecord.of(it.destination, now, it.change);
	}

	private static <T> FlowChangeEntry<T> calculate(Calculation<T> calculation, FlowStateLookup lookup, Duration duration) {
		Change<T> change = calculation.evaluate(lookup, duration);
		return new FlowChangeEntry<>(calculation.destination(), change);
	}

	private static class FlowChangeEntry<T> {
		private final FlowId<T> destination;
		private final Change<T> change;
		public FlowChangeEntry(FlowId<T> destination, Change<T> change) {
			this.destination = destination;
			this.change = change;
		}

		public FlowId<T> destination() {
			return destination;
		}

		public Change<T> change() {
			return change;
		}
	}

	public static ImmutableEngine.Builder builder() {
		return ImmutableEngine.builder();
	}
}
