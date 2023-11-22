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