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

import de.flapdoodle.cashflows.tests.AsciiArt;
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