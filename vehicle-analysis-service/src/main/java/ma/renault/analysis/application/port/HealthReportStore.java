package ma.renault.analysis.application.port;

import java.util.Optional;
import ma.renault.analysis.domain.HealthReport;

public interface HealthReportStore {
	void save(HealthReport report);

	Optional<HealthReport> findByVin(String vin);
}
