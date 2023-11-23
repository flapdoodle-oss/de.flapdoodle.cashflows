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

public class AsciiArt {
	private final double minX;
	private final double minY;
	private final double deltaX;
	private final double deltaY;

	private AsciiArt(double minX, double maxX) {
		this(minX, maxX, 0.0, 1.0);
	}

	private AsciiArt(double minX, double maxX, double minY, double maxY) {
		this.minX = minX;
		this.minY = minY;
		this.deltaX = maxX - minX;
		this.deltaY = maxY - minY;
	}

	public interface MapDouble {
		double map(double x);
	}

	public String asAsciiArt(int size, MapDouble function) {
		return asAsciiArt(size, size, function);
	}

	public String asAsciiArt(int width, int heigth, MapDouble function) {
		double[] values = new double[width + 1];
		for (int i = 0; i <= width; i++) {
			double x = 1.0d * i / width;
			values[i] = function.map(x * deltaX + minX);
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i <= width + 2; i++) {
			sb.append("-");
		}
		sb.append("\n");
		for (int y = heigth; y >= 0; y--) {
			double relUpperLimitY = 1.0d * y / heigth;
			double relLowerLimitY = 1.0d * (y - 1) / heigth;
			double upperLimitY = relUpperLimitY*deltaY+minY;
			double lowerLimitY = relLowerLimitY*deltaY+minY;

			sb.append("|");
			for (int x = 0; x <= width; x++) {
				double value = values[x];
				if (value > lowerLimitY && value <= upperLimitY) {
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

	public static AsciiArt with(double minX, double maxX, double minY, double maxY) {
		return new AsciiArt(minX, maxX, minY, maxY);
	}

	public static AsciiArt with(double minX, double maxX) {
		return new AsciiArt(minX, maxX);
	}

	public static AsciiArt fromZeroToOne() {
		return new AsciiArt(0, 1);
	}
}
