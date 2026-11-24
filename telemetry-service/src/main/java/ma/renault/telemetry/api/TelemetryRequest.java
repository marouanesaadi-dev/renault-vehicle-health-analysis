package ma.renault.telemetry.api;

import jakarta.validation.constraints.*;
import java.time.Instant;
import java.util.List;

public record TelemetryRequest(
        @NotBlank @Pattern(regexp = "[A-HJ-NPR-TV-Z0-9]{17}") String vin,
        @NotNull Instant timestamp,
        @PositiveOrZero long mileageKm,
        @NotEmpty List<@Positive Double> tirePressuresBar,
        @NotNull @Positive Double batteryVoltage,
        @NotNull @Positive Double engineTemperatureC,
        @Min(0) @Max(100) double fuelLevelPercent,
        @NotNull List<@NotBlank String> errorCodes) { }
