package de.flapdoodle.cashflows.generators;

import de.flapdoodle.checks.Preconditions;
import org.immutables.value.Value;

import java.util.List;

@Value.Immutable
public abstract class LinearInterpolation<T> {
	protected abstract double min();
	protected abstract double max();
	protected abstract List<T> values();
	protected abstract Fader<T> fader();

	@Value.Check
	protected void check() {
		Preconditions.checkArgument(!values().isEmpty(),"no values set");
		Preconditions.checkArgument(min()!=max(),"min(%s) == max(%s)", min(), max());
	}

	public T get(double x) {
		Preconditions.checkArgument(min()<=x,"%s to smaller than %s", x, min());
		Preconditions.checkArgument(x<=max(),"%s to bigger than %s", x, max());
		if (values().size()==1) {
			return values().get(0);
		}
		double range=max()-min();
		double relativePosition=((x-min())*(values().size()-1)/range);
		int pos=(int) relativePosition;
		double offsetBetweenValues = relativePosition - pos;

		Preconditions.checkArgument(offsetBetweenValues >= 0.0, "%s <= 0", offsetBetweenValues);
		Preconditions.checkArgument(offsetBetweenValues <= 1.0, "%s >= 1.0", offsetBetweenValues);

		if (pos==values().size() - 1) {
			pos=pos-1;
			offsetBetweenValues=1.0;
		}

		T left = values().get(pos);
		T right = values().get(pos + 1);

		return fader()
			.fadeBetween(offsetBetweenValues, left, right);
	}

	public static <T> LinearInterpolation<T> of(double min, double max, Fader<T> fader, Iterable<? extends T> values) {
		return ImmutableLinearInterpolation.<T>builder()
			.min(min)
			.max(max)
			.fader(fader)
			.addAllValues(values)
			.build();
	}

	@FunctionalInterface
	public interface Fader<T> {
		T fadeBetween(double balance, T left, T right);
	}
}
