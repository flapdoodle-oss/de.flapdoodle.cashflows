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
