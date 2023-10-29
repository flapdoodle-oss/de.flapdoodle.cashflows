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

	@Value.Lazy
	protected double deltaX() {
		return maxX() - minX();
	}

	@Value.Lazy
	protected double deltaY() {
		return maxY() - minY();
	}

	@Value.Check
	protected void check() {
		Preconditions.checkArgument(minX() < maxX(), "minX>=maxX: %s >= %s", minX(), maxX());
		Preconditions.checkArgument(minY() < maxY(), "minY>=maxY: %s >= %s", minY(), maxY());
	}

	@Value.Auxiliary
	public Position mapTo(Position position, Area destination) {
		double destX = (position.x() - minX()) * destination.deltaX() / deltaX() + destination.minX();
		double destY = (position.y() - minY()) * destination.deltaY() / deltaY() + destination.minY();
		return Position.of(destX, destY);
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
