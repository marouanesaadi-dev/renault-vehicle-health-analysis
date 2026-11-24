package ma.renault.telemetry.api;

import java.util.Map;
import ma.renault.telemetry.application.TelemetryService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/telemetry")
public class TelemetryController {
    private final TelemetryService service;
    public TelemetryController(TelemetryService service) { this.service=service; }
    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Map<String,String> receive(@RequestBody @jakarta.validation.Valid TelemetryRequest request) throws com.fasterxml.jackson.core.JsonProcessingException {
        return Map.of("telemetryId", service.accept(request), "status", "ACCEPTED");
    }
}
