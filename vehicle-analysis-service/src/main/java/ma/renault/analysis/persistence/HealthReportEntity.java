package ma.renault.analysis.persistence;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;

@Entity
public class HealthReportEntity {
	@Id
	private String vin;
	@Lob
	private String reportJson;

	protected HealthReportEntity() {
	}

	public HealthReportEntity(String vin, String reportJson) {
		this.vin = vin;
		this.reportJson = reportJson;
	}

	public String getReportJson() {
		return reportJson;
	}
}
