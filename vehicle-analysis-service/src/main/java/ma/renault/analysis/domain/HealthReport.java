package ma.renault.analysis.domain;

import java.time.Instant;
import java.util.List;

public record HealthReport(String vin, int healthScore, String healthStatus, Instant analyzedAt, List<Alert> alerts,
		List<String> appliedImpacts) {
}
