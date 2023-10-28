package de.flapdoodle.cashflows.generators;

import de.flapdoodle.checks.Preconditions;

@FunctionalInterface
public interface Generator {
	double map(double x);

	static Generator noise(long seed, double freq) {
		return x -> assertRange((OpenSimplex2.noise2(seed, x*freq, 0.0) + 1.0) / 2.0);
	}

	static double map(Generator generator, double x, double min, double max) {
		double factor = generator.map(x);
		return factor * (max - min) + min;
	}

	static double assertRange(double x) {
		Preconditions.checkArgument(x >= 0.0, "%s <= 0", x);
		Preconditions.checkArgument(x <= 1.0, "%s >= 1.0", x);
		return x;
	}
}
