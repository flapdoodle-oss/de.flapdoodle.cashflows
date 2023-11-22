package de.flapdoodle.cashflows.generators;

import de.flapdoodle.checks.Preconditions;

@FunctionalInterface
public interface Ease {
	double map(double x);

	static double linear(double x) {
		return assertRange(x);
	}

	static double easeInOutQuad(double x) {
		assertRange(x);
		return assertRange(x < 0.5
			? 2 * x * x
			: 1 - Math.pow(-2 * x + 2, 2) / 2);
	}

	static double easeInOutCubic(double x) {
		assertRange(x);

		return assertRange(x < 0.5
			? 4 * x * x * x
			: 1 - Math.pow(-2 * x + 2, 3) / 2);
	}

	static double sinusUpDown(double x) {
		assertRange(x);

		return assertRange((Math.sin(x * Math.PI * 2.0 - Math.PI / 2.0) + 1.0) / 2.0);
	}

	static Ease mirrorX(Ease delegate) {
		return x -> {
			assertRange(x);
			return x < 0.5
				? delegate.map(x * 2.0)
				: delegate.map(1.0 - ((x - 0.5) * 2.0));
		};
	}

	static Ease flipX(Ease delegate) {
		return x -> delegate.map(1.0 - x);
	}

	static Ease flipY(Ease delegate) {
		return x -> 1.0 - delegate.map(x);
	}

	static double map(Ease ease, double x, double min, double max) {
		double factor = ease.map(x);
		return factor * (max - min) + min;
	}

	static double assertRange(double x) {
		Preconditions.checkArgument(x >= 0.0, "%s <= 0", x);
		Preconditions.checkArgument(x <= 1.0, "%s >= 1.0", x);
		return x;
	}

	static double[] assertRange(double [] array) {
		for (int i=0;i<array.length;i++) {
			Preconditions.checkArgument(array[i] >= 0.0, "array[%s]: %s <= 0", i, array[i]);
			Preconditions.checkArgument(array[i] <= 1.0, "array[%s]: %s >= 1.0", i, array[i]);
		}
		return array;
	}

	enum Interpolation {
		Linear
	}

	static Ease interpolated(Interpolation interpolation, boolean repeatable, double ... points) {
		return InterpolatedEase.interpolated(interpolation,repeatable,points);
	}
}
