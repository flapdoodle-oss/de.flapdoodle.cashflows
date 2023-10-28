package de.flapdoodle.cashflows.generators;

public class AsciiArt {
	private final double minX;
	private final double maxX;

	private AsciiArt(double minX, double maxX) {
		this.minX = minX;
		this.maxX = maxX;
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
			values[i] = function.map(x * (maxX - minX) + minX);
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

	public static AsciiArt with(double minX, double maxX) {
		return new AsciiArt(minX, maxX);
	}

	public static AsciiArt fromZeroToOne() {
		return new AsciiArt(0, 1);
	}
}
