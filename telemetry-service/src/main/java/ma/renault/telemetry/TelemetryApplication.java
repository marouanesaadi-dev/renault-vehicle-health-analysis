package ma.renault.telemetry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class TelemetryApplication {
    public static void main(String[] args) { SpringApplication.run(TelemetryApplication.class, args); }
}
