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
package de.flapdoodle.cashflows.tests;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AsciiGraphTest {

	@Test
	void drawIntoEachCorner() {
		String ascii = AsciiGraph.of(Area.of(-1.0, 1.0, -1.0, 1.0))
			.render(8, 8, renderContext -> {
				renderContext.point('1', -1.0,-1.0);
				renderContext.point('2', 1.0,1.0);
				renderContext.point('3', -1.0,1.0);
				renderContext.point('4', 1.0,-1.0);
			});
		assertThat(ascii).isEqualTo(""
			+ "+--------+\n"
			+ "|3      2|\n"
			+ "|        |\n"
			+ "|        |\n"
			+ "|        |\n"
			+ "|        |\n"
			+ "|        |\n"
			+ "|        |\n"
			+ "|1      4|\n"
			+ "+--------+");
	}

	@Test
	void mappingTest() {
		String ascii = AsciiGraph.of(Area.of(0, 23, -1, 2))
			.render(24, 8, renderContext -> {
				renderContext.point('1', 0,-1);
				renderContext.point('2', 23,-1);
				renderContext.point('3', 23,2);
				renderContext.point('4', 0,2);

				for (int i=0;i<24;i++) {
					renderContext.point('*', i,0);
				}
				for (double i=-1;i<=2.0;i=i+0.25) {
					renderContext.point('°', 2,i);
				}
			});
		assertThat(ascii).isEqualTo(""
			+ "+------------------------+\n"
			+ "|4 °                    3|\n"
			+ "|  °                     |\n"
			+ "|  °                     |\n"
			+ "|  °                     |\n"
			+ "|  °                     |\n"
			+ "|**°*********************|\n"
			+ "|  °                     |\n"
			+ "|1 °                    2|\n"
			+ "+------------------------+");
	}

	@Test
	void drawCircle() {
		String ascii = AsciiGraph.of(Area.of(-1.0, 1.0, -1.0, 1.0))
			.render(32, 16, renderContext -> {
				for (int i = 0; i < 360; i = i + 1) {
					double x = Math.sin(Math.toRadians(i));
					double y = Math.cos(Math.toRadians(i));
					renderContext.point('*', x, y);
				}
			});
		assertThat(ascii).isEqualTo(""
			+ "+--------------------------------+\n"
			+ "|               *                |\n"
			+ "|       *****************        |\n"
			+ "|     ***               ***      |\n"
			+ "|   **                     **    |\n"
			+ "| **                         **  |\n"
			+ "|**                           ** |\n"
			+ "|*                             * |\n"
			+ "|*                             * |\n"
			+ "|*                             **|\n"
			+ "|*                             * |\n"
			+ "|*                             * |\n"
			+ "|**                           ** |\n"
			+ "| **                         **  |\n"
			+ "|   **                     **    |\n"
			+ "|     ***               ***      |\n"
			+ "|       *****************        |\n"
			+ "+--------------------------------+");
	}
}