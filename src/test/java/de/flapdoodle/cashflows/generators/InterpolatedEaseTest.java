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

import org.assertj.core.data.Percentage;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class InterpolatedEaseTest {

	@Test
	void linearInterpolationWithOnePoint() {
		Ease testee = InterpolatedEase.linearInterpolation(0.5);

		assertThat(testee.map(0))
			.isCloseTo(0.5, Percentage.withPercentage(1.0));
		assertThat(testee.map(0.0))
			.isCloseTo(0.5, Percentage.withPercentage(1.0));
		assertThat(testee.map(1.0))
			.isCloseTo(0.5, Percentage.withPercentage(1.0));
	}

	@Test
	void linearInterpolationWithTwoPoints() {
		Ease testee = InterpolatedEase.linearInterpolation(0, 1.0);

		assertThat(testee.map(0))
			.isCloseTo(0, Percentage.withPercentage(1.0));
		assertThat(testee.map(0.5))
			.isCloseTo(0.5, Percentage.withPercentage(1.0));
		assertThat(testee.map(1.0))
			.isCloseTo(1.0, Percentage.withPercentage(1.0));
	}

	@Test
	void linearInterpolationWithThreePoints() {
		Ease testee = InterpolatedEase.linearInterpolation(0, 1.0, 0.5);

		assertThat(testee.map(0))
			.isCloseTo(0, Percentage.withPercentage(1.0));
		assertThat(testee.map(0.25))
			.isCloseTo(0.5, Percentage.withPercentage(1.0));
		assertThat(testee.map(0.5))
			.isCloseTo(1.0, Percentage.withPercentage(1.0));
		assertThat(testee.map(0.75))
			.isCloseTo(0.75, Percentage.withPercentage(1.0));
		assertThat(testee.map(1.0))
			.isCloseTo(0.5, Percentage.withPercentage(1.0));


	}
}