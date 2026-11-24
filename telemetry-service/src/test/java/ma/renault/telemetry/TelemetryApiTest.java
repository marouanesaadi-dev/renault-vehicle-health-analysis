package ma.renault.telemetry;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ma.renault.telemetry.application.TelemetryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest
class TelemetryApiTest {
    @Autowired MockMvc mvc;
    @MockBean TelemetryService service;

    @Test
    void acceptsValidTelemetryAndStartsAnalysis() throws Exception {
        when(service.accept(any())).thenReturn("test-id");
        mvc.perform(post("/api/v1/telemetry").contentType(MediaType.APPLICATION_JSON).content("""
                {"vin":"VF1AAAAA123456789","timestamp":"2026-11-24T10:00:00Z","mileageKm":42000,
                 "tirePressuresBar":[2.3,2.4,2.2,2.3],"batteryVoltage":12.6,"engineTemperatureC":90,
                 "fuelLevelPercent":40,"errorCodes":[]}
                """))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.status").value("ACCEPTED"))
                .andExpect(jsonPath("$.telemetryId").isNotEmpty());
        verify(service).accept(any());
    }

    @Test
    void rejectsInvalidVin() throws Exception {
        mvc.perform(post("/api/v1/telemetry").contentType(MediaType.APPLICATION_JSON).content("""
                {"vin":"bad","timestamp":"2026-11-24T10:00:00Z","mileageKm":0,
                 "tirePressuresBar":[2.3],"batteryVoltage":12.6,"engineTemperatureC":90,
                 "fuelLevelPercent":40,"errorCodes":[]}
                """))
                .andExpect(status().isBadRequest());
    }
}
