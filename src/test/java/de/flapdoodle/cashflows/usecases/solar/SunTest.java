package de.flapdoodle.cashflows.usecases.solar;

import de.flapdoodle.cashflows.generators.AsciiArt;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class SunTest {

	@Test
	void dayLenght() {
		assertThat(AsciiArt.with(0, 365, 7.0, 18.0)
			.asAsciiArt(32, 8, x -> Sun.dayLength((int) (x))))
			.isEqualTo(""
				+ "-----------------------------------\n"
				+ "|               ****              |\n"
				+ "|            ***    **            |\n"
				+ "|          **         **          |\n"
				+ "|         *             *         |\n"
				+ "|       **               **       |\n"
				+ "|      *                   *      |\n"
				+ "|    **                     **    |\n"
				+ "|****                         ****|\n"
				+ "|                                 |\n"
				+ "-----------------------------------");
	}

	@Test
	void sunIntensity() {
		assertThat(AsciiArt.with(0, 24*60, -0.1, 1.1)
			.asAsciiArt(32, 8, x -> Sun.sunIntensity(300, (int) x)))
			.isEqualTo(""
				+ "-----------------------------------\n"
				+ "|               ***               |\n"
				+ "|              *   *              |\n"
				+ "|             *     *             |\n"
				+ "|            *       *            |\n"
				+ "|           *         *           |\n"
				+ "|                                 |\n"
				+ "|          *           *          |\n"
				+ "|**********             **********|\n"
				+ "|                                 |\n"
				+ "-----------------------------------");

		assertThat(AsciiArt.with(0, 24*60, -0.1, 1.1)
			.asAsciiArt(32, 8, x -> Sun.sunIntensity(180, (int) x)))
			.isEqualTo(""
				+ "-----------------------------------\n"
				+ "|              *****              |\n"
				+ "|            **     **            |\n"
				+ "|          **         **          |\n"
				+ "|         *             *         |\n"
				+ "|        *               *        |\n"
				+ "|       *                 *       |\n"
				+ "|      *                   *      |\n"
				+ "|******                     ******|\n"
				+ "|                                 |\n"
				+ "-----------------------------------");
	}

	@Test
	void sunPerKWpYear() {
		assertThat(AsciiArt.with(0, 365, 0.0, 4.0)
			.asAsciiArt(32, 8, x -> Sun.pvPerKWp((int)(x)).value()))
			.isEqualTo(""
				+ "-----------------------------------\n"
				+ "|              *****              |\n"
				+ "|            **     **            |\n"
				+ "|          **         **          |\n"
				+ "|         *             *         |\n"
				+ "|       **               **       |\n"
				+ "|     **                   **     |\n"
				+ "|   **                       **   |\n"
				+ "|***                           ***|\n"
				+ "|                                 |\n"
				+ "-----------------------------------");
	}

}