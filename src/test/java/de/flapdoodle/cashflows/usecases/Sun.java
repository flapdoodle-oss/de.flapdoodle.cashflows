package de.flapdoodle.cashflows.usecases;

import de.flapdoodle.cashflows.types.Ease;

public abstract class Sun {

	public static double normal(int dayOfTheYear, double winter, double summer) {
		double factor = Ease.sinusUpDown(dayOfTheYear / 365.0);
		return winter + (summer - winter) * factor;
	}
}
