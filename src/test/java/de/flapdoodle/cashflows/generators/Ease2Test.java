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

import de.flapdoodle.cashflows.tests.Area;
import de.flapdoodle.cashflows.tests.AsciiArt;
import de.flapdoodle.cashflows.tests.AsciiGraph;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class Ease2Test {
	@Test
	public void blend() {
		Ease left = Ease.interpolated(Ease.Interpolation.Linear, false, 0.0, 1.0, 0.5);
		Ease right = Ease.interpolated(Ease.Interpolation.Linear, false, 1.0, 0.0, 0.5);
		Ease blend = Ease::linear;
		Ease2 testee = Ease2.blend(left, right, blend);

//		Ease testee = Ease.interpolated(Ease.Interpolation.Linear, false, 0.0, 1.0, 0.5);

		assertThat(AsciiGraph.of(Area.of(0, 1, 0, 1))
			.render(32, 10, renderContext -> {
				char[] samples = "*+#".toCharArray();

				for (int b = 0; b < samples.length; b++) {
					for (double x = 0; x <= 1.0; x = x + 0.01) {
						renderContext.point(samples[b], x, testee.map(x, ((double) b) / (samples.length-1)));
					}
				}
			}))
			.isEqualTo(""
				+ "+--------------------------------+\n"
				+ "|#                               |\n"
				+ "|##           ******             |\n"
				+ "| ###        **     ****         |\n"
				+ "|   ##     **          ****      |\n"
				+ "|     ## ***               ****  |\n"
				+ "|+++++++##++++++++++++++++++++## |\n"
				+ "|     ** ###               ####  |\n"
				+ "|   **     ##          ####      |\n"
				+ "| ***        ##     ####         |\n"
				+ "|**           ######             |\n"
				+ "+--------------------------------+");
	}

	private static String asAsciiArt(int width, int heigth, Ease easeFunction) {
		return AsciiArt.fromZeroToOne().asAsciiArt(width, heigth, easeFunction::map);
	}
}