package ma.renault.telemetry.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TelemetryRepository extends JpaRepository<TelemetryEntity, String> { }
