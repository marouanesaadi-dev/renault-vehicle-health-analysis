package ma.renault.analysis.api;

import java.util.List;
import ma.renault.analysis.domain.Alert;
import ma.renault.analysis.domain.HealthReport;
import ma.renault.analysis.service.AnalysisService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class AnalysisController {
    private final AnalysisService service;
    public AnalysisController(AnalysisService service) { this.service=service; }
    @PostMapping("/internal/analysis")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public HealthReport analyze(@RequestBody @jakarta.validation.Valid TelemetryRequest request) { return service.analyze(request); }
    @GetMapping("/api/v1/vehicles/{vin}/health-report")
    public HealthReport report(@PathVariable String vin) { return requireReport(vin); }
    @GetMapping("/api/v1/vehicles/{vin}/alerts")
    public List<Alert> alerts(@PathVariable String vin) { return requireReport(vin).alerts(); }
    private HealthReport requireReport(String vin) {
        return service.report(vin).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"No analysis available for VIN"));
    }
}
