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
package de.flapdoodle.cashflows.usecases.solar;

import de.flapdoodle.cashflows.tests.Area;
import de.flapdoodle.cashflows.tests.AsciiArt;
import de.flapdoodle.cashflows.tests.AsciiGraph;
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
	void sunIntensity() {
		assertThat(AsciiGraph.of(Area.of(0, 24*60, -0.1, 1.1))
				.render(48, 12, renderContext -> {
					for (int minuteOfDay = 0; minuteOfDay < 24*60; minuteOfDay=minuteOfDay+10) {
						renderContext.point('-', minuteOfDay, Sun.sunIntensity(300, minuteOfDay));
						renderContext.point('*', minuteOfDay, Sun.sunIntensity(180, minuteOfDay));
					}
				}))
			.isEqualTo(""
				+ "+------------------------------------------------+\n"
				+ "|                                                |\n"
				+ "|                      ***                       |\n"
				+ "|                  ****- -****                   |\n"
				+ "|                ***--     --***                 |\n"
				+ "|              **  -         -  **               |\n"
				+ "|             **  -           -  **              |\n"
				+ "|           ***  -             -  ***            |\n"
				+ "|          **   --             --   **           |\n"
				+ "|         **    -               -    **          |\n"
				+ "|        **    -                 -    **         |\n"
				+ "|       *     --                 --     *        |\n"
				+ "|*******-------                   -------******* |\n"
				+ "+------------------------------------------------+");
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

	@Test
	void zenith() {
		assertThat(AsciiArt.with(0, 365, 10, 70)
			.asAsciiArt(32, 8, x -> Sun.zenith((int)(x))))
			.isEqualTo(""
				+ "-----------------------------------\n"
				+ "|                                 |\n"
				+ "|             *******             |\n"
				+ "|           **       **           |\n"
				+ "|         **           **         |\n"
				+ "|        *               *        |\n"
				+ "|      **                 **      |\n"
				+ "|    **                     **    |\n"
				+ "|****                         ****|\n"
				+ "|                                 |\n"
				+ "-----------------------------------");
	}

	@Test
	void zenithOfDay() {
		assertThat(AsciiArt.with(300*24, 303*24, 0, 50)
			.asAsciiArt(64, 10, hourOfYear -> {
				int dayOfYear = (int) hourOfYear / 24;
				int minuteOfDay = (((int) hourOfYear) % 24)*60;
				return Sun.zenith(dayOfYear) * Sun.sunIntensity(dayOfYear, minuteOfDay);
			}))
			.isEqualTo(""
				+ "-------------------------------------------------------------------\n"
				+ "|                                                                 |\n"
				+ "|                                                                 |\n"
				+ "|                                                                 |\n"
				+ "|                                                                 |\n"
				+ "|           *                    *                     *          |\n"
				+ "|         ** **                 * **                 ** *         |\n"
				+ "|        *     *               *    *                             |\n"
				+ "|                                                   *    *        |\n"
				+ "|               *             *      *             *      *       |\n"
				+ "|       *                    *        *           *        *      |\n"
				+ "|*******         ************          ***********          ******|\n"
				+ "-------------------------------------------------------------------");
	}
}