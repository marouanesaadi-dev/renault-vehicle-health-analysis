package ma.renault.telemetry.persistence;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import java.time.Instant;

@Entity
public class TelemetryEntity {
	@Id
	private String id;
	private String vin;
	private Instant receivedAt;
	@Lob
	private String payloadJson;

	protected TelemetryEntity() {
	}

	public TelemetryEntity(String id, String vin, Instant receivedAt, String payloadJson) {
		this.id = id;
		this.vin = vin;
		this.receivedAt = receivedAt;
		this.payloadJson = payloadJson;
	}
}
