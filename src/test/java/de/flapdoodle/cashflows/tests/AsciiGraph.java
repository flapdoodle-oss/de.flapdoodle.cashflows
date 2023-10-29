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

		withRenderContext.accept((s, x, y) -> {
			Position rel = area.relativePosition(Position.of(x, y));
			if (rel.x() >= 0 && rel.x() <= 1.0) {
				if (rel.y() >= 0 && rel.y() <= 1.0) {
					int w = (int) (rel.x() * (width - 1));
					int h = (int) (rel.y() * (height - 1));
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

	interface RenderContext {
		void point(char s, double x, double y);
	}
}
