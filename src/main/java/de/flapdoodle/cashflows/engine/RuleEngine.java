package de.flapdoodle.cashflows.engine;

import com.google.common.collect.Streams;
import de.flapdoodle.cashflows.iterator.LinearIterator;
import de.flapdoodle.cashflows.records.History;
import de.flapdoodle.formula.calculate.MappedValue;
import de.flapdoodle.formula.calculate.StrictValueLookup;
import de.flapdoodle.formula.rules.Rules;
import de.flapdoodle.formula.solver.Result;
import de.flapdoodle.formula.solver.Solver;
import de.flapdoodle.formula.solver.ValueDependencyGraphBuilder;
import de.flapdoodle.formula.solver.ValueGraph;
import org.immutables.builder.Builder;
import org.immutables.value.Value;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Value.Immutable
public abstract class RuleEngine<T extends Comparable<? super T>> {
	@Builder.Parameter
	protected abstract LinearIterator<T> iterator();
	protected abstract de.flapdoodle.formula.Value<Iteration<T>> iteratorValue();  // last, now, delta?

	protected abstract Rules iterationRules();
	protected abstract List<Aggregation<?>> aggregations();

	public static <T extends Comparable<? super T>> ImmutableRuleEngine.Builder<T> builder(LinearIterator<T> iterator) {
		return ImmutableRuleEngine.builder(iterator);
	}

	@Value.Auxiliary
	public History<T> run(T start, T end) {
		ValueGraph graph = ValueDependencyGraphBuilder.build(iterationRules());
		History<T> history = History.with(iterator(), start);

		int loop=0;
		T last=iterator().next(start, loop);
		T current=iterator().next(start, loop);

		history=history.change(loop, changeListener -> {
			aggregations().forEach(it -> setStartValue(changeListener, it));
		});

		do {
			System.out.println("> "+current);

			StrictValueLookup valueLookup = StrictValueLookup.of(
				Streams.concat(
					mappedValues(history, loop, aggregations()).stream(),
					Stream.of(MappedValue.of(iteratorValue(), Iteration.of(last, current)))
				).collect(Collectors.toList())
			);

			Result solved = Solver.solve(graph, valueLookup);

			history=history.change(loop+1, changeListener -> {
				aggregations().forEach(it -> aggregate(changeListener, solved, it));
			});

			loop++;
			last=current;
			current=iterator().next(start, loop);
		} while (current.compareTo(end) < 0);

		return history;
	}

	private static <T> void aggregate(History.ChangeListener changeListener, Result result, Aggregation<T> it) {
		T delta = result.get(it.delta());
		T base = changeListener.last(it.base());
		T newBase = it.aggregate().apply(base, delta);
		changeListener.set(it.base(), newBase);
	}

	private static List<? extends MappedValue<?>> mappedValues(final History<?> history, final int loop, List<Aggregation<?>> aggregations) {
		return aggregations.stream()
			.map(it -> mappedValue(history, loop, it))
			.collect(Collectors.toList());
	}

	private static <T> MappedValue<T> mappedValue(History<?> history, int offset, Aggregation<T> it) {
		return MappedValue.of(it.base(), history.get(offset, it.base()));
	}

	private static <T> void setStartValue(History.ChangeListener changeListener, Aggregation<T> it) {
		changeListener.set(it.base(), it.start());
	}
}
