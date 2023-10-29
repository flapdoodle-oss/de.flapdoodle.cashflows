package de.flapdoodle.cashflows.tests;

import de.flapdoodle.checks.Preconditions;
import org.immutables.value.Value;

@Value.Immutable
public abstract class Area {
	@Value.Default
	public double minX() {
		return 0.0;
	}

	@Value.Default
	public double maxX() {
		return 1.0;
	}

	@Value.Default
	public double minY() {
		return 0.0;
	}

	@Value.Default
	public double maxY() {
		return 1.0;
	}

	@Value.Check
	protected void check() {
		Preconditions.checkArgument(minX() < maxX(), "minX>=maxX: %s >= %s", minX(), maxX());
		Preconditions.checkArgument(minY() < maxY(), "minY>=maxY: %s >= %s", minY(), maxY());
	}

	@Value.Auxiliary
	public Position relativePosition(Position p) {
		double deltaX = maxX() - minX();
		double deltaY = maxY() - minY();

		return Position.of((p.x() - minX()) / deltaX, (p.y() - minY()) / deltaY);
	}

	public static ImmutableArea of(double minX, double maxX, double minY, double maxY) {
		return ImmutableArea.builder()
			.minX(minX)
			.maxX(maxX)
			.minY(minY)
			.maxY(maxY)
			.build();
	}

	public static ImmutableArea zeroToOne() {
		return ImmutableArea.builder().build();
	}
}
