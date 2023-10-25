package de.flapdoodle.cashflows.calculation;

import de.flapdoodle.cashflows.types.Change;
import de.flapdoodle.cashflows.types.FlowId;
import de.flapdoodle.cashflows.types.FlowState;
import org.immutables.value.Value;

import java.time.Duration;

public interface Calculation<T> {
	FlowId<T> destination();
	Change<T> evaluate(FlowStateLookup flowStateLookup, Duration duration);

	interface Arg0<T> {
		Change<T> evaluate(Duration duration);
	}

	interface Arg1<S, T> {
		Change<T> evaluate(FlowState<S> source, Duration duration);
	}

	interface Arg2<A, B, T> {
		Change<T> evaluate(FlowState<A> a, FlowState<B> b, Duration duration);
	}

	@Value.Immutable
	abstract class Generator<T> implements Calculation<T> {
		protected abstract Arg0<T> transformation();

		@Override
		public Change<T> evaluate(FlowStateLookup flowStateLookup, Duration duration) {
			return transformation().evaluate(duration);
		}
	}

	@Value.Immutable
	abstract class Single<S, T> implements Calculation<T> {
		protected abstract FlowId<S> source();
		protected abstract Arg1<S, T> transformation();

		@Override
		public Change<T> evaluate(FlowStateLookup flowStateLookup, Duration duration) {
			FlowState<S> s = flowStateLookup.stateOf(source());
			return transformation().evaluate(s, duration);
		}
	}

	@Value.Immutable
	abstract class Merge2<A, B, T> implements Calculation<T> {
		protected abstract FlowId<A> a();
		protected abstract FlowId<B> b();
		protected abstract Arg2<A, B, T> transformation();

		@Override
		public Change<T> evaluate(FlowStateLookup flowStateLookup, Duration duration) {
			FlowState<A> a = flowStateLookup.stateOf(a());
			FlowState<B> b = flowStateLookup.stateOf(b());
			return transformation().evaluate(a, b, duration);
		}
	}

	static <T> Generator<T> of(FlowId<T> dest, Arg0<T> transformation) {
		return ImmutableGenerator.<T>builder()
			.destination(dest)
			.transformation(transformation)
			.build();
	}

	static <S, T> Single<S, T> of(FlowId<T> dest, FlowId<S> source, Arg1<S, T> transformation) {
		return ImmutableSingle.<S, T>builder()
			.destination(dest)
			.source(source)
			.transformation(transformation)
			.build();
	}

	static <A, B, T> Merge2<A, B, T> of(FlowId<T> dest, FlowId<A> a, FlowId<B> b, Arg2<A, B, T> transformation) {
		return ImmutableMerge2.<A, B, T>builder()
			.destination(dest)
			.a(a)
			.b(b)
			.transformation(transformation)
			.build();
	}
}
