package de.flapdoodle.cashflows.usecases.solar;

import de.flapdoodle.cashflows.generators.Ease;
import de.flapdoodle.cashflows.usecases.types.KWh;

public abstract class Sun {

	private static double normal(int dayOfTheYear, double winter, double summer) {
		return Ease.map(Ease::sinusUpDown, dayOfTheYear/365.0, winter, summer);
	}

	public static double sunIntensity(int dayOfTheYear, int minuteOfDay) {
		double dayLength = dayLength(dayOfTheYear);
		double dayStart = (24.0 - dayLength) / 2.0;
		double now=minuteOfDay / 60.0;
		double nowAsRelativePositionOfTheDay = (now - dayStart) / dayLength;

		return nowAsRelativePositionOfTheDay <= 0 || nowAsRelativePositionOfTheDay >= 1.0
			? 0.0
			: Math.sin(nowAsRelativePositionOfTheDay * Math.PI);
	}

	public static double dayLength(int dayOfTheYear) {
		return normal(dayOfTheYear, 7.5, 17);
	}

	// Beispiele aus dem Netz
	// 10kWp -> 1200kWh pro Monat - Sommer
	// 10kWp -> 837kWh pro Monat - Winter
	public static KWh pvPerKWp(int dayOfYear) {
		double maxkWhproKW = 110.0 / 30.0;
		double minkWhproKW = 8.37 / 30.0;
		return KWh.of(Ease.map(Ease::sinusUpDown, dayOfYear / 365.0, minkWhproKW, maxkWhproKW));
	}

	// http://www.solartopo.com/sonnenumlaufbahn.htm
	public static double zenith(int dayOfYear) {
		return normal(dayOfYear, 12.77, 59.58);
	}
}
