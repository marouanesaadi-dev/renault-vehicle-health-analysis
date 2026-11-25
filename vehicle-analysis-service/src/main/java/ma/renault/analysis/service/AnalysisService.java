package ma.renault.analysis.service;

import java.util.Optional;
import ma.renault.analysis.api.TelemetryRequest;
import ma.renault.analysis.domain.HealthReport;
import ma.renault.analysis.domain.HealthScoreCalculator;
import ma.renault.analysis.application.port.HealthReportStore;
import ma.renault.analysis.application.port.RecallProvider;
import org.springframework.stereotype.Service;

@Service
public class AnalysisService {
	private final RecallProvider recallProvider;
	private final HealthScoreCalculator calculator;
	private final HealthReportStore reportStore;

	public AnalysisService(RecallProvider recallProvider, HealthScoreCalculator calculator,
			HealthReportStore reportStore) {
		this.recallProvider = recallProvider;
		this.calculator = calculator;
		this.reportStore = reportStore;
	}

	public HealthReport analyze(TelemetryRequest telemetry) {
		HealthReport report = calculator.calculate(telemetry, recallProvider.hasActiveRecall(telemetry.vin()));
		reportStore.save(report);
		return report;
	}

	public Optional<HealthReport> report(String vin) {
		return reportStore.findByVin(vin);
	}
}
