package ma.renault.telemetry.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Instant;
import java.util.UUID;
import ma.renault.telemetry.api.TelemetryRequest;
import ma.renault.telemetry.persistence.TelemetryEntity;
import ma.renault.telemetry.persistence.TelemetryRepository;
import ma.renault.telemetry.service.AnalysisClient;
import org.springframework.stereotype.Service;

@Service
public class TelemetryService {
    private final TelemetryRepository repository;
    private final AnalysisClient analysisClient;
    private final ObjectMapper mapper;
    public TelemetryService(TelemetryRepository repository,AnalysisClient analysisClient,ObjectMapper mapper) {
        this.repository=repository; this.analysisClient=analysisClient; this.mapper=mapper;
    }
    public String accept(TelemetryRequest request) throws JsonProcessingException {
        String id=UUID.randomUUID().toString();
        repository.save(new TelemetryEntity(id,request.vin(),Instant.now(),mapper.writeValueAsString(request)));
        analysisClient.analyze(request);
        return id;
    }
}
