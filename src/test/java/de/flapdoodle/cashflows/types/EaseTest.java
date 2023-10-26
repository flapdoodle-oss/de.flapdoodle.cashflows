package de.flapdoodle.cashflows.types;

import org.junit.jupiter.api.Test;

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

	private static String asAsciiArt(int size, Ease easeFunction) {
		return asAsciiArt(size, size, easeFunction);
	}

	private static String asAsciiArt(int width, int heigth, Ease easeFunction) {
		double[] values = new double[width + 1];
		for (int i = 0; i <= width; i++) {
			double x = 1.0d * i / width;
			values[i] = easeFunction.map(x);
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i <= width + 2; i++) {
			sb.append("-");
		}
		sb.append("\n");
		for (int y = heigth; y >= 0; y--) {
			double maxY = 1.0d * y / heigth;
			double minY = 1.0d * (y - 1) / heigth;

			sb.append("|");
			for (int x = 0; x <= width; x++) {
				double value = values[x];
				if (value > minY && value <= maxY) {
					sb.append("*");
				} else {
					sb.append(" ");
				}
			}
			sb.append("|\n");
		}
		for (int i = 0; i <= width + 2; i++) {
			sb.append("-");
		}
		return sb.toString();
	}
}