package ma.renault.analysis.api;

import java.time.Instant;
import java.util.List;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public record TelemetryRequest(@NotBlank @Pattern(regexp = "[A-HJ-NPR-TV-Z0-9]{17}") String vin,
		@NotNull Instant timestamp, @PositiveOrZero long mileageKm, @NotEmpty List<Double> tirePressuresBar,
		@NotNull @Positive Double batteryVoltage, @NotNull @Positive Double engineTemperatureC,
		@Min(0) @Max(100) double fuelLevelPercent, @NotNull List<String> errorCodes) {
	@AssertTrue(message = "Tire pressures must be positive")
	public boolean isTirePressuresPositive() {
		return tirePressuresBar == null || tirePressuresBar.stream().allMatch(value -> value != null && value > 0);
	}

	@AssertTrue(message = "Error codes must not be blank")
	public boolean isErrorCodesNonBlank() {
		return errorCodes == null || errorCodes.stream().allMatch(value -> value != null && !value.isBlank());
	}
}
