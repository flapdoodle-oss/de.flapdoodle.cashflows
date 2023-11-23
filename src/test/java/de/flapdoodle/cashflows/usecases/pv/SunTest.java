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
package de.flapdoodle.cashflows.usecases.pv;

import de.flapdoodle.cashflows.tests.AsciiArt;
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