package de.flapdoodle.cashflows.usescases.pv;

import de.flapdoodle.cashflows.generators.Ease;

public abstract class Sun {

	public static double normal(int dayOfTheYear, double winter, double summer) {
		double factor = Ease.sinusUpDown(dayOfTheYear / 365.0);
		return winter + (summer - winter) * factor;
	}

	public static double dayLength(int dayOfTheYear) {
		return normal(dayOfTheYear, 7.5, 17);
	}
}
