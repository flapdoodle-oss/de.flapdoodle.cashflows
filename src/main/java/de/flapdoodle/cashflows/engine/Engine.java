package de.flapdoodle.cashflows.engine;

import de.flapdoodle.cashflows.calculation.Calculation;
import de.flapdoodle.cashflows.calculation.FlowStateLookup;
import de.flapdoodle.cashflows.calculation.Transaction;
import de.flapdoodle.cashflows.iterator.LinearIterator;
import de.flapdoodle.cashflows.records.Record;
import de.flapdoodle.cashflows.records.Records;
import de.flapdoodle.cashflows.types.Change;
import de.flapdoodle.cashflows.types.Flow;
import de.flapdoodle.cashflows.types.FlowId;
import de.flapdoodle.cashflows.types.Range;
import de.flapdoodle.checks.Preconditions;
import org.immutables.builder.Builder;
import org.immutables.value.Value;

import java.util.*;

@Value.Immutable
public abstract class Engine<T extends Comparable<? super T>> {
	@Builder.Parameter
	protected abstract LinearIterator<T> iterator();

	protected abstract List<Flow<?>> flows();
	protected abstract List<Transaction<T>> transactions();

	public Records<T> calculate(T start, T end) {
		Preconditions.checkArgument(start.compareTo(end) < 0,"%s >= %s", start, end);

		Records<T> records = Records.of(start, iterator(), flows());

		Map<Transaction<T>, Integer> lastRun=new LinkedHashMap<>();
		Map<Transaction<T>, Range<T>> activeRange=new LinkedHashMap<>();
		transactions().forEach(t -> activeRange.put(t, t.section()));

		int loop=0;
		T current=iterator().next(start, loop);

		do {
			List<FlowChangeEntry<?>> changes=new ArrayList<>();

			for (Transaction<T> transaction : transactions()) {
				Range<T> range = activeRange.get(transaction);
				while (range.isActive(current)) {
					int transactionLastRun = Optional.ofNullable(lastRun.get(transaction)).orElse(loop);
					int latestResultsBeforeCurrent = loop;

					FlowStateLookup flowStateLookup = records.stateLookupOf(transactionLastRun, latestResultsBeforeCurrent);

					for (Calculation<T, ?> calculation : transaction.calculations()) {
						FlowChangeEntry<?> entry = calculate(calculation, flowStateLookup, iterator().next(start, transactionLastRun), current);
						changes.add(entry);
					}

					lastRun.put(transaction, loop);
					range=range.nextRange();
				}
				activeRange.put(transaction, range);
			}

			records = merge(records, loop, changes);

			loop++;
			current=iterator().next(start, loop);
		} while (current.compareTo(end) < 0);

		return records;
	}

	private Records<T> merge(Records<T> records, int offset, List<FlowChangeEntry<?>> changes) {
		Records<T> current = records;
		for (FlowChangeEntry<?> it : changes) {
			current=current.add(asRecord(offset, it));
		}
		return current;
	}

	private static <T> Record<T> asRecord(int offset, FlowChangeEntry<T> it) {
		return Record.of(it.destination(), offset, it.change());
	}

	private static <I extends Comparable<? super I>, T> FlowChangeEntry<T> calculate(Calculation<I, T> calculation, FlowStateLookup lookup, I lastRun, I now) {
		Change<T> change = calculation.evaluate(lastRun, now, lookup);
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

	public static <T extends Comparable<? super T>> ImmutableEngine.Builder<T> builder(LinearIterator<T> iterator) {
		return ImmutableEngine.builder(iterator);
	}
}
