package de.flapdoodle.cashflows.usescases.pv;

import de.flapdoodle.cashflows.generators.AsciiArt;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SunTest {

	@Test
	void dayLenght() {
		assertThat(AsciiArt.with(0, 1.0, 7.0, 18.0)
			.asAsciiArt(32, 8, x -> Sun.dayLength((int) (x * 365.0))))
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
}