package ma.renault.analysis.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import ma.renault.analysis.application.port.RecallProvider;

@Component
public class RecallClient implements RecallProvider {
	private final RestClient client;

	public RecallClient(@Value("${recall-provider.url:http://localhost:8090}") String url) {
		client = RestClient.builder().baseUrl(url).build();
	}

	@Override
	public boolean hasActiveRecall(String vin) {
		RecallResponse response = client.get().uri("/api/recalls/{vin}", vin).retrieve().body(RecallResponse.class);
		return response != null && response.activeRecall();
	}

	public record RecallResponse(boolean activeRecall) {
	}
}
