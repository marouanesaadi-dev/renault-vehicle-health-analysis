package ma.renault.telemetry.service;

import ma.renault.telemetry.api.TelemetryRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class AnalysisClient {
	private final RestClient client;

	public AnalysisClient(@Value("${analysis-service.url:http://localhost:8082}") String url) {
		this.client = RestClient.builder().baseUrl(url).build();
	}

	@Async
	public void analyze(TelemetryRequest request) {
		client.post().uri("/internal/analysis").body(request).retrieve().toBodilessEntity();
	}
}
