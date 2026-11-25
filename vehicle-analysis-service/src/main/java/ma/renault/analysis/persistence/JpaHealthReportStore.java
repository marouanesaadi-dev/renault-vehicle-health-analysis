package ma.renault.analysis.persistence;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Optional;
import ma.renault.analysis.application.port.HealthReportStore;
import ma.renault.analysis.domain.HealthReport;
import org.springframework.stereotype.Repository;

@Repository
public class JpaHealthReportStore implements HealthReportStore {
	private final HealthReportRepository repository;
	private final ObjectMapper mapper;

	public JpaHealthReportStore(HealthReportRepository repository, ObjectMapper mapper) {
		this.repository = repository;
		this.mapper = mapper;
	}

	@Override
	public void save(HealthReport report) {
		try {
			repository.save(new HealthReportEntity(report.vin(), mapper.writeValueAsString(report)));
		} catch (JsonProcessingException exception) {
			throw new IllegalStateException("Health report cannot be serialized", exception);
		}
	}

	@Override
	public Optional<HealthReport> findByVin(String vin) {
		return repository.findById(vin).map(entity -> {
			try {
				return mapper.readValue(entity.getReportJson(), HealthReport.class);
			} catch (JsonProcessingException exception) {
				throw new IllegalStateException("Stored health report cannot be read", exception);
			}
		});
	}
}
