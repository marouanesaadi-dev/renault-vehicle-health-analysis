package ma.renault.analysis.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Instant;
import java.util.List;
import ma.renault.analysis.api.TelemetryRequest;
import org.junit.jupiter.api.Test;

class HealthScoreCalculatorTest {
    private final HealthScoreCalculator calculator=new HealthScoreCalculator();

    @Test
    void healthyVehicleStartsAtOneHundred() {
        HealthReport report=calculator.calculate(telemetry(12.6,90,60_000,List.of(),List.of(2.3,2.4,2.2,2.4)),false);
        assertEquals(95,report.healthScore());
    }

    @Test
    void appliesMileageBatteryErrorAndRecallPenalties() {
        HealthReport report=calculator.calculate(telemetry(11.7,112,155_000,List.of("P0420","P0301"),List.of(2.3,2.4,2.2,2.4)),true);
        assertEquals(30,report.healthScore());
        assertEquals("POOR",report.healthStatus());
    }

    @Test
    void scoreNeverFallsBelowZero() {
        HealthReport report=calculator.calculate(telemetry(11.0,120,200_000,List.of("A","B","C","D","E"),List.of(1.5,1.5,1.5,1.5)),true);
        assertEquals(0,report.healthScore());
    }

    private TelemetryRequest telemetry(double battery,double temp,long mileage,List<String> errors,List<Double> tires) {
        return new TelemetryRequest("VF1AAAAA123456789",Instant.parse("2026-11-24T10:00:00Z"),mileage,tires,battery,temp,40,errors);
    }
}
