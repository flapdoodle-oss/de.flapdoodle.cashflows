package de.flapdoodle.cashflows.generators;

import de.flapdoodle.cashflows.tests.AsciiArt;
import de.flapdoodle.cashflows.usecases.types.KWh;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

class LinearInterpolationTest {

	@Test
	void interpolation() {
		LinearInterpolation.Fader<KWh> fader= (balance, left, right) -> KWh.of(left.value() * (1.0-balance) + right.value() * balance);
		LinearInterpolation<KWh> testee = LinearInterpolation.of(3, 5, fader, Arrays.asList(
			KWh.of(0),
			KWh.of(1.0),
			KWh.of(0.5)
		));

		assertThat(testee.get(3))
			.isEqualTo(KWh.of(0));
		assertThat(testee.get(5))
			.isEqualTo(KWh.of(0.5));
		assertThat(testee.get(4))
			.isEqualTo(KWh.of(1.0));
	}

	@Test
	public void linearInterpolated() {
		LinearInterpolation.Fader<KWh> fader= (balance, left, right) -> KWh.of(left.value() * (1.0-balance) + right.value() * balance);
		LinearInterpolation<KWh> testee = LinearInterpolation.of(3, 5, fader, Arrays.asList(
			KWh.of(0),
			KWh.of(1.0),
			KWh.of(0.5)
		));

		assertThat(AsciiArt.with(3, 5, 0, 1)
			.asAsciiArt(64, 16, x -> testee.get(x).value()))
			.isEqualTo(""
				+ "-------------------------------------------------------------------\n"
				+ "|                               *****                             |\n"
				+ "|                             **     ****                         |\n"
				+ "|                           **           ****                     |\n"
				+ "|                         **                 ****                 |\n"
				+ "|                       **                       ****             |\n"
				+ "|                     **                             ****         |\n"
				+ "|                   **                                   ****     |\n"
				+ "|                 **                                         **** |\n"
				+ "|               **                                               *|\n"
				+ "|             **                                                  |\n"
				+ "|           **                                                    |\n"
				+ "|         **                                                      |\n"
				+ "|       **                                                        |\n"
				+ "|     **                                                          |\n"
				+ "|   **                                                            |\n"
				+ "| **                                                              |\n"
				+ "|*                                                                |\n"
				+ "-------------------------------------------------------------------");
	}
}