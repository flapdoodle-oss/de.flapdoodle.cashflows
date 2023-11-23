package de.flapdoodle.cashflows.usecases.solar;

import com.google.common.collect.ImmutableList;
import de.flapdoodle.cashflows.generators.Ease;
import de.flapdoodle.cashflows.generators.Ease2;
import de.flapdoodle.cashflows.usecases.types.KWh;

import java.util.Arrays;

public class House {

	private static Ease winterHourlyConsumption() {
		ImmutableList.of(
			KWh.of(0.13), // 0:00
			KWh.of(0.13),
			KWh.of(0.13),
			KWh.of(0.13),
			KWh.of(0.13),
			KWh.of(0.13),
			KWh.of(0.39), // 6:00
			KWh.of(0.39),
			KWh.of(0.39),
			KWh.of(0.08),
			KWh.of(0.11),
			KWh.of(0.13),
			KWh.of(0.40), // 12:00
			KWh.of(0.13),
			KWh.of(0.11),
			KWh.of(0.19),
			KWh.of(0.29),
			KWh.of(0.40),
			KWh.of(0.3), // 18:00
			KWh.of(0.4),
			KWh.of(0.5),
			KWh.of(0.5),
			KWh.of(0.5),
			KWh.of(0.12) // 23:00
		);
//		return Ease.interpolated(Ease.Interpolation.Linear, true,
//			0
//		);
		return null;
	}

	private static double normal(int dayOfTheYear, Ease winter, Ease summer) {
//		return Ease.map(Ease::sinusUpDown, dayOfTheYear/365.0, winter, summer);
		return 0;
	}

}
