package de.flapdoodle.cashflows.usecases;

import de.flapdoodle.cashflows.aggregations.FlowRecords;
import de.flapdoodle.cashflows.calculation.Calculation;
import de.flapdoodle.cashflows.calculation.Transaction;
import de.flapdoodle.cashflows.engine.Engine;
import de.flapdoodle.cashflows.report.GroupByDate2SystemOut;
import de.flapdoodle.cashflows.types.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;

public class PVCaseTest {

	@Test
	public void compareModels() {
		LocalDate now=LocalDate.of(2023, Month.OCTOBER,23);

		FlowType<KWh> kWhFlowType = FlowType.of(KWh.class, KWh::reduce);

		FlowId<KWh> pv = FlowId.of("PV", kWhFlowType);
		FlowId<KWh> grid = FlowId.of("Grid", kWhFlowType);
		FlowId<KWh> battery = FlowId.of("Battery", kWhFlowType);
		FlowId<KWh> houseConsumption = FlowId.of("Consumption", kWhFlowType);

		DateRange eachDay = DateRange.of(now, it -> it.plusDays(1));

//
//		FlowId<Double> source = FlowId.of("source", FlowType.DOUBLE);
//		FlowId<Double> dest = FlowId.of("dest", FlowType.DOUBLE);
//
//
		Engine engine = Engine.builder()
			.addFlows(Flow.of(pv, KWh.of(0)))
//			.addFlows(Flow.of(dest, 0d))
			.addTransactions(Transaction.builder()
				.section(eachDay)
//				.addCalculations(Calculation.of(dest, source, (s, duration) -> s.after() >= 100.0
//					? Change.of("move 100 from source", 100.0)
//					: Change.of("not enough", 0.0)))
//				.addCalculations(Calculation.of(source, source, dest, (s, d, duration) -> s.after() >= 100.0
//					? Change.of("fix moved 100", -100.0)
//					: Change.of("dont fix", 0.0)))
				.build())
			.build();

		FlowRecords result = engine.calculate(now, now.plusDays(10));

////		assertThat(result.stateOf(source, now.plusDays(10)))
////			.isEqualTo(FlowState.of(55.0, 60.0));
//
		new GroupByDate2SystemOut()
			.render(result, Arrays.asList(pv));
	}
}
