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

import java.util.Arrays;
import java.util.function.Consumer;

public class AsciiGraph {
	private final Area area;

	private AsciiGraph(Area area) {
		this.area = area;
	}

	public static AsciiGraph of(Area area) {
		return new AsciiGraph(area);
	}

	public String render(int width, int height, Consumer<RenderContext> withRenderContext) {
		char[] content = new char[width * height];
		char[] line = new char[width];
		Arrays.fill(content, ' ');
		Arrays.fill(line, '-');

		Area renderArea = Area.of(0.0, width-1.0, 0.0, height-1.0);

		withRenderContext.accept((s, x, y) -> {
			Position dest = area.mapTo(Position.of(x, y), renderArea);
			if (dest.x()>=0 && dest.x()<=width) {
				if (dest.y()>=0 && dest.y()<=height) {
					int h = (int) dest.y();
					int w = (int) dest.x();
					int index = (height - 1 - h) * width + w;
					content[index] = s;
				}
			}
		});

		StringBuilder sb = new StringBuilder();

		sb.append("+").append(line).append("+\n");
		for (int i = 0; i < height; i++) {
			sb.append("|");
			sb.append(content, i * width, width);
			sb.append("|\n");
		}
		sb.append("+").append(line).append("+");
		return sb.toString();
	}

	public interface RenderContext {
		void point(char s, double x, double y);
	}
}
