package de.flapdoodle.cashflows.generators;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class GeneratorTest {
	
	@Test
	public void noise() {
		assertThat(asAsciiArt(64, 16, 2.0, Generator.noise(1337, 3.0)))
			.isEqualTo(""
				+ "-------------------------------------------------------------------\n"
				+ "|                    **                *                          |\n"
				+ "|  **               *  *              * *                         |\n"
				+ "|                 **    *                            **          *|\n"
				+ "| *              *                   *   *             *          |\n"
				+ "|    *          *                                   *   *         |\n"
				+ "|                        *          *                           * |\n"
				+ "|              *                   *      *              *        |\n"
				+ "|                         *       *                               |\n"
				+ "|*    *       *                                    *      *       |\n"
				+ "|                          *     *         *                   *  |\n"
				+ "|            *              *                              *      |\n"
				+ "|                            *  *           *     *           *   |\n"
				+ "|      *    *                 **             *              *     |\n"
				+ "|          *                                                 *    |\n"
				+ "|       * *                                   *  *                |\n"
				+ "|        *                                     **                 |\n"
				+ "|                                                                 |\n"
				+ "-------------------------------------------------------------------");
	}

	private static String asAsciiArt(int width, int heigth, double maxX, Generator function) {
		return AsciiArt.with(0.0, maxX).asAsciiArt(width, heigth, function::map);
	}

}