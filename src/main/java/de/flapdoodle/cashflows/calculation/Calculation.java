package de.flapdoodle.cashflows.calculation;

import de.flapdoodle.cashflows.types.Change;
import de.flapdoodle.cashflows.types.FlowId;
import de.flapdoodle.cashflows.types.FlowState;
import org.immutables.value.Value;

public interface Calculation<I, T> {
	FlowId<T> destination();
	Change<T> evaluate(I lastRun, I now, FlowStateLookup flowStateLookup);

	interface Arg0<I, T> {
		Change<T> evaluate(I lastRun, I now);
	}

	interface Arg1<I, S, T> {
		Change<T> evaluate(I lastRun, I now, FlowState<S> source);
	}

	interface Arg2<I, A, B, T> {
		Change<T> evaluate(I lastRun, I now, FlowState<A> a, FlowState<B> b);
	}

	@Value.Immutable
	abstract class Generator<I, T> implements Calculation<I, T> {
		protected abstract Arg0<I, T> transformation();

		@Override
		public Change<T> evaluate(I lastRun, I now, FlowStateLookup flowStateLookup) {
			return transformation().evaluate(lastRun, now);
		}
	}

	@Value.Immutable
	abstract class Single<I, T, S> implements Calculation<I, T> {
		protected abstract FlowId<S> source();
		protected abstract Arg1<I, S, T> transformation();

		@Override
		public Change<T> evaluate(I lastRun, I now, FlowStateLookup flowStateLookup) {
			FlowState<S> s = flowStateLookup.stateOf(source());
			return transformation().evaluate(lastRun, now, s);
		}
	}

	@Value.Immutable
	abstract class Merge2<I, T, A, B> implements Calculation<I, T> {
		protected abstract FlowId<A> a();
		protected abstract FlowId<B> b();
		protected abstract Arg2<I, A, B, T> transformation();

		@Override
		public Change<T> evaluate(I lastRun, I now, FlowStateLookup flowStateLookup) {
			FlowState<A> a = flowStateLookup.stateOf(a());
			FlowState<B> b = flowStateLookup.stateOf(b());
			return transformation().evaluate(lastRun, now, a, b);
		}
	}

	static <I, T> Generator<I, T> of(FlowId<T> dest, Arg0<I, T> transformation) {
		return ImmutableGenerator.<I, T>builder()
			.destination(dest)
			.transformation(transformation)
			.build();
	}

	static <I, T, S> Single<I, T, S> of(FlowId<T> dest, FlowId<S> source, Arg1<I, S, T> transformation) {
		return ImmutableSingle.<I, T, S>builder()
			.destination(dest)
			.source(source)
			.transformation(transformation)
			.build();
	}

	static <I, T, A, B> Merge2<I, T, A, B> of(FlowId<T> dest, FlowId<A> a, FlowId<B> b, Arg2<I, A, B, T> transformation) {
		return ImmutableMerge2.<I, T, A, B>builder()
			.destination(dest)
			.a(a)
			.b(b)
			.transformation(transformation)
			.build();
	}
}
