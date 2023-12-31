/*
 * Copyright (C) 2022
 *   Michael Mosmann <michael@mosmann.de>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
