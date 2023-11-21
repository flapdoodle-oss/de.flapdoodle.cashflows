package de.flapdoodle.cashflows.engine;

import de.flapdoodle.checks.Preconditions;
import de.flapdoodle.reflection.TypeInfo;
import org.immutables.value.Value;

@Value.Immutable
public interface Iteration<T> {
	@Value.Parameter
	T last();

	@Value.Parameter
	T current();

	static <T> Iteration<T> of(T last, T current) {
		return ImmutableIteration.of(last, current);
	}

	@Value.Immutable
	abstract class IterationTypeInfo<T> implements TypeInfo<Iteration<T>> {
		@Value.Parameter
		protected abstract TypeInfo<T> type();

		@Override
		public Iteration<T> cast(Object instance) {
			Preconditions.checkArgument(isInstance(instance),"");
			return (Iteration<T>) instance;
		}

		@Override
		public boolean isInstance(Object instance) {
			return instance instanceof Iteration
				&& type().isInstance(((Iteration<?>) instance).last())
				&& type().isInstance(((Iteration<?>) instance).current());
		}
	}

	static <T> TypeInfo<Iteration<T>> typeInfo(TypeInfo<T> type) {
		return ImmutableIterationTypeInfo.of(type);
	}
}
