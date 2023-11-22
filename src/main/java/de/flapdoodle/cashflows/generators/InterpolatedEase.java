package de.flapdoodle.cashflows.generators;

import de.flapdoodle.checks.Preconditions;
import org.jheaps.annotations.VisibleForTesting;

abstract class InterpolatedEase {

	static Ease interpolated(Ease.Interpolation interpolation, boolean repeatable, double ... points) {
		double[] copy = repeatable
			? new double[points.length + 1]
			: new double[points.length];
		System.arraycopy(points, 0, copy, 0, points.length);
		if (repeatable) {
			copy[copy.length-1]=copy[0];
		}

		switch (interpolation) {
			case Linear:
				return linearInterpolation(copy);
			default:
				throw new IllegalArgumentException("not implemented: "+interpolation);
		}
	}

	@VisibleForTesting
	static Ease linearInterpolation(double ... copy) {
		Preconditions.checkArgument(copy.length > 0, "empty array not allowed");

		Ease.assertRange(copy);
		if (copy.length == 1) {
			return x -> {
				Ease.assertRange(x);
				return copy[0];
			};
		}
		if (copy.length == 2) {
			return x -> {
				Ease.assertRange(x);
				return (copy[0] * (1.0-x) + copy[1] * x);
			};
		}
		return x -> {
			double relativePosition = (copy.length - 1) * x;
			int pos=(int) relativePosition;
			double offsetBetweenPoints = relativePosition - pos;
			if (pos==copy.length-1) {
				return copy[copy.length-1];
			}
			double left = copy[pos] * (1.0 - offsetBetweenPoints);
			double right = copy[pos + 1] * offsetBetweenPoints;
			return left + right;
		};
	}

}
