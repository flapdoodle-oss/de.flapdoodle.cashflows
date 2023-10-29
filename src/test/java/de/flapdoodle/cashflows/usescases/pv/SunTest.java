package de.flapdoodle.cashflows.usescases.pv;

import de.flapdoodle.cashflows.generators.AsciiArt;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

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
	void sunshineHours() {
		assertThat(AsciiArt.with(0, 365, 1, 11.0)
			.asAsciiArt(32, 8, x -> Sun.sunshineHours((int) (x))))
			.isEqualTo(""
				+ "-----------------------------------\n"
				+ "|               ***               |\n"
				+ "|            ***   ***            |\n"
				+ "|          **         **          |\n"
				+ "|         *             *         |\n"
				+ "|        *               *        |\n"
				+ "|      **                 **      |\n"
				+ "|    **                     **    |\n"
				+ "|****                         ****|\n"
				+ "|                                 |\n"
				+ "-----------------------------------");
	}

	@Test
	void cloudyHours() {
		assertThat(AsciiArt.with(0, 365.0, 6.0, 8.0)
			.asAsciiArt(32, 8, x -> Sun.cloudyHours((int) x)))
			.isEqualTo(""
				+ "-----------------------------------\n"
				+ "|                                 |\n"
				+ "|                                 |\n"
				+ "|                                 |\n"
				+ "|                                 |\n"
				+ "|          *************          |\n"
				+ "|      ****             ****      |\n"
				+ "|******                     ******|\n"
				+ "|                                 |\n"
				+ "|                                 |\n"
				+ "-----------------------------------");
	}

	@Test
	void pv() {
		for (int i=0;i<365;i=i+30) {
			System.out.println(Sun.pvPerKWp(i).multiply(8.25));
		}
	}
}