package ma.renault.analysis;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import java.time.Instant;
import java.util.List;
import ma.renault.analysis.api.TelemetryRequest;
import ma.renault.analysis.domain.HealthReport;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

@SpringBootTest(webEnvironment=SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties="spring.datasource.url=jdbc:h2:mem:analysis-test;DB_CLOSE_DELAY=-1")
class AnalysisIntegrationTest {
    private static final WireMockServer provider=new WireMockServer(WireMockConfiguration.options().dynamicPort());
    static { provider.start(); }
    @Autowired TestRestTemplate http;

    @AfterAll static void stopProvider() { provider.stop(); }
    @DynamicPropertySource static void providerUrl(DynamicPropertyRegistry registry) { registry.add("recall-provider.url",provider::baseUrl); }

    @Test
    void analyzesTelemetryCallsExternalProviderAndExposesReportAndAlerts() {
        provider.stubFor(get(urlPathEqualTo("/api/recalls/VF1AAAAA123456789"))
                .willReturn(aResponse().withStatus(200).withHeader("Content-Type","application/json")
                        .withBody("{\"activeRecall\":true}")));
        TelemetryRequest request=new TelemetryRequest("VF1AAAAA123456789",Instant.parse("2026-11-24T10:00:00Z"),120_000,
                List.of(2.3,2.2,2.4,2.3),12.4,90,40,List.of("P0420"));

        ResponseEntity<HealthReport> accepted=http.postForEntity("/internal/analysis",request,HealthReport.class);
        assertThat(accepted.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
        ResponseEntity<HealthReport> report=http.getForEntity("/api/v1/vehicles/{vin}/health-report",HealthReport.class,request.vin());
        assertThat(report.getBody().healthScore()).isEqualTo(75);
        assertThat(report.getBody().alerts()).extracting("code").contains("ACTIVE_RECALL","ERROR_CODES");
        provider.verify(getRequestedFor(urlPathEqualTo("/api/recalls/VF1AAAAA123456789")));
    }
}
