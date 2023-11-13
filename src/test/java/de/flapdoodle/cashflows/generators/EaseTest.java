package de.flapdoodle.cashflows.generators;

import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

class EaseTest {

	@Test
	public void linearTest() {
		assertThat(asAsciiArt(8, Ease::linear))
			.isEqualTo(""
				+ "-----------\n"
				+ "|        *|\n"
				+ "|       * |\n"
				+ "|      *  |\n"
				+ "|     *   |\n"
				+ "|    *    |\n"
				+ "|   *     |\n"
				+ "|  *      |\n"
				+ "| *       |\n"
				+ "|*        |\n"
				+ "-----------");
	}

	@Test
	public void flipX() {
		assertThat(asAsciiArt(8, Ease.flipX(Ease::linear)))
			.isEqualTo(""
				+ "-----------\n"
				+ "|*        |\n"
				+ "| *       |\n"
				+ "|  *      |\n"
				+ "|   *     |\n"
				+ "|    *    |\n"
				+ "|     *   |\n"
				+ "|      *  |\n"
				+ "|       * |\n"
				+ "|        *|\n"
				+ "-----------");
	}

	@Test
	public void flipY() {
		assertThat(asAsciiArt(8, Ease.flipY(Ease::linear)))
			.isEqualTo(""
				+ "-----------\n"
				+ "|*        |\n"
				+ "| *       |\n"
				+ "|  *      |\n"
				+ "|   *     |\n"
				+ "|    *    |\n"
				+ "|     *   |\n"
				+ "|      *  |\n"
				+ "|       * |\n"
				+ "|        *|\n"
				+ "-----------");
	}

	@Test
	public void easeInOutQuad() {
		assertThat(asAsciiArt(64, 16, Ease::easeInOutQuad))
			.isEqualTo(""
				+ "-------------------------------------------------------------------\n"
				+ "|                                                     ************|\n"
				+ "|                                                 ****            |\n"
				+ "|                                             ****                |\n"
				+ "|                                          ***                    |\n"
				+ "|                                       ***                       |\n"
				+ "|                                     **                          |\n"
				+ "|                                   **                            |\n"
				+ "|                                 **                              |\n"
				+ "|                              ***                                |\n"
				+ "|                            **                                   |\n"
				+ "|                          **                                     |\n"
				+ "|                       ***                                       |\n"
				+ "|                    ***                                          |\n"
				+ "|                 ***                                             |\n"
				+ "|            *****                                                |\n"
				+ "| ***********                                                     |\n"
				+ "|*                                                                |\n"
				+ "-------------------------------------------------------------------");
	}

	@Test
	public void easeInOutCubic() {
		assertThat(asAsciiArt(64, 16, Ease::easeInOutCubic))
			.isEqualTo(""
				+ "-------------------------------------------------------------------\n"
				+ "|                                                 ****************|\n"
				+ "|                                            *****                |\n"
				+ "|                                         ***                     |\n"
				+ "|                                       **                        |\n"
				+ "|                                     **                          |\n"
				+ "|                                   **                            |\n"
				+ "|                                  *                              |\n"
				+ "|                                 *                               |\n"
				+ "|                               **                                |\n"
				+ "|                              *                                  |\n"
				+ "|                            **                                   |\n"
				+ "|                          **                                     |\n"
				+ "|                        **                                       |\n"
				+ "|                     ***                                         |\n"
				+ "|                 ****                                            |\n"
				+ "| ****************                                                |\n"
				+ "|*                                                                |\n"
				+ "-------------------------------------------------------------------");
	}

	@Test
	public void mirrorX() {
		assertThat(asAsciiArt(64, 16, Ease.mirrorX(Ease::easeInOutCubic)))
			.isEqualTo(""
				+ "-------------------------------------------------------------------\n"
				+ "|                         ***************                         |\n"
				+ "|                      ***               ***                      |\n"
				+ "|                     *                     *                     |\n"
				+ "|                    *                       *                    |\n"
				+ "|                   *                         *                   |\n"
				+ "|                  *                           *                  |\n"
				+ "|                 *                             *                 |\n"
				+ "|                                                                 |\n"
				+ "|                *                               *                |\n"
				+ "|               *                                 *               |\n"
				+ "|              *                                   *              |\n"
				+ "|             *                                     *             |\n"
				+ "|            *                                       *            |\n"
				+ "|           *                                         *           |\n"
				+ "|         **                                           **         |\n"
				+ "| ********                                               ******** |\n"
				+ "|*                                                               *|\n"
				+ "-------------------------------------------------------------------");
	}

	@Test
	public void sinus() {
		assertThat(asAsciiArt(64, 16, Ease::sinusUpDown))
			.isEqualTo(""
				+ "-------------------------------------------------------------------\n"
				+ "|                           ***********                           |\n"
				+ "|                         **           **                         |\n"
				+ "|                       **               **                       |\n"
				+ "|                      *                   *                      |\n"
				+ "|                    **                     **                    |\n"
				+ "|                   *                         *                   |\n"
				+ "|                  *                           *                  |\n"
				+ "|                 *                             **                |\n"
				+ "|               **                                *               |\n"
				+ "|              *                                   *              |\n"
				+ "|             *                                     *             |\n"
				+ "|           **                                       **           |\n"
				+ "|          *                                           *          |\n"
				+ "|        **                                             **        |\n"
				+ "|      **                                                 **      |\n"
				+ "| *****                                                     ***** |\n"
				+ "|*                                                               *|\n"
				+ "-------------------------------------------------------------------");
	}

	@Test
	public void gausianNoise() {
		Random random = new Random(1337);
		assertThat(asAsciiArt(64, 16, x -> random.nextGaussian()))
			.isEqualTo(""
				+ "-------------------------------------------------------------------\n"
				+ "|                                                             *   |\n"
				+ "|                                                                 |\n"
				+ "|                                                                 |\n"
				+ "|                                                                 |\n"
				+ "|                *                                        *       |\n"
				+ "|                               *          *                      |\n"
				+ "|                                                                 |\n"
				+ "|                                  *                              |\n"
				+ "|     *              *                                       *    |\n"
				+ "|                                            *                    |\n"
				+ "|                           *                                     |\n"
				+ "|                                                                 |\n"
				+ "|                                               *   * *           |\n"
				+ "|          *  *     *                                             |\n"
				+ "|                              *                           *      |\n"
				+ "|            *             *                   *         *        |\n"
				+ "|       *                                 *                       |\n"
				+ "-------------------------------------------------------------------");
	}

	@Test
	public void map() {
		assertThat(asAsciiArt(64, 16, x -> Ease.map(Ease.mirrorX(Ease::easeInOutCubic), x, 1, 0)))
			.isEqualTo(""
				+ "-------------------------------------------------------------------\n"
				+ "|********                                                 ********|\n"
				+ "|        ***                                           ***        |\n"
				+ "|           *                                         *           |\n"
				+ "|            *                                       *            |\n"
				+ "|             *                                     *             |\n"
				+ "|              *                                   *              |\n"
				+ "|               *                                 *               |\n"
				+ "|                                                                 |\n"
				+ "|                *                               *                |\n"
				+ "|                 *                             *                 |\n"
				+ "|                  *                           *                  |\n"
				+ "|                   *                         *                   |\n"
				+ "|                    *                       *                    |\n"
				+ "|                     *                     *                     |\n"
				+ "|                      **                 **                      |\n"
				+ "|                        ******** ********                        |\n"
				+ "|                                *                                |\n"
				+ "-------------------------------------------------------------------");
	}

	private static String asAsciiArt(int size, Ease easeFunction) {
		return asAsciiArt(size, size, easeFunction);
	}

	private static String asAsciiArt(int width, int heigth, Ease easeFunction) {
		return AsciiArt.fromZeroToOne().asAsciiArt(width, heigth, easeFunction::map);
	}
}