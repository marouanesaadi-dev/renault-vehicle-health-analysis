package ma.renault.analysis.domain;

import java.util.ArrayList;
import java.util.List;
import ma.renault.analysis.api.TelemetryRequest;

public class HealthScoreCalculator {
	public HealthReport calculate(TelemetryRequest t, boolean activeRecall) {
		int score = 100;
		List<Alert> alerts = new ArrayList<>();
		List<String> impacts = new ArrayList<>();

		if (t.mileageKm() >= 150_000) {
			score -= 15;
			add(alerts, impacts, "HIGH_MILEAGE", "MEDIUM", "Kilométrage supérieur ou égal à 150 000 km", 15);
		} else if (t.mileageKm() >= 100_000) {
			score -= 10;
			add(alerts, impacts, "HIGH_MILEAGE", "LOW", "Kilométrage supérieur ou égal à 100 000 km", 10);
		} else if (t.mileageKm() >= 50_000) {
			score -= 5;
			add(alerts, impacts, "MILEAGE", "INFO", "Kilométrage supérieur ou égal à 50 000 km", 5);
		}

		for (int i = 0; i < t.tirePressuresBar().size(); i++) {
			double p = t.tirePressuresBar().get(i);
			if (p < 1.8) {
				score -= 10;
				add(alerts, impacts, "TIRE_PRESSURE_CRITICAL", "HIGH", "Pression critique au pneu " + (i + 1), 10);
			} else if (p < 2.0 || p > 2.8) {
				score -= 5;
				add(alerts, impacts, "TIRE_PRESSURE", "MEDIUM", "Pression hors plage au pneu " + (i + 1), 5);
			}
		}
		if (t.batteryVoltage() < 11.8) {
			score -= 15;
			add(alerts, impacts, "BATTERY_CRITICAL", "HIGH", "Tension batterie inférieure à 11,8 V", 15);
		} else if (t.batteryVoltage() < 12.2) {
			score -= 5;
			add(alerts, impacts, "BATTERY_LOW", "MEDIUM", "Tension batterie inférieure à 12,2 V", 5);
		}
		if (t.engineTemperatureC() > 110) {
			score -= 20;
			add(alerts, impacts, "ENGINE_OVERHEAT", "HIGH", "Température moteur supérieure à 110 °C", 20);
		} else if (t.engineTemperatureC() > 100) {
			score -= 10;
			add(alerts, impacts, "ENGINE_HOT", "MEDIUM", "Température moteur supérieure à 100 °C", 10);
		}
		if (t.fuelLevelPercent() < 10) {
			score -= 5;
			add(alerts, impacts, "LOW_FUEL", "LOW", "Niveau de carburant inférieur à 10 %", 5);
		}
		int codePenalty = Math.min(t.errorCodes().stream().distinct().toList().size() * 5, 20);
		if (codePenalty > 0) {
			score -= codePenalty;
			add(alerts, impacts, "ERROR_CODES", "HIGH",
					"Codes défaut : " + String.join(", ", t.errorCodes().stream().distinct().toList()), codePenalty);
		}
		if (activeRecall) {
			score -= 10;
			add(alerts, impacts, "ACTIVE_RECALL", "HIGH", "Une campagne de rappel active concerne le véhicule", 10);
		}

		score = Math.max(0, Math.min(100, score));
		String status = score >= 80 ? "GOOD" : score >= 50 ? "FAIR" : "POOR";
		return new HealthReport(t.vin(), score, status, java.time.Instant.now(), List.copyOf(alerts),
				List.copyOf(impacts));
	}

	private void add(List<Alert> alerts, List<String> impacts, String code, String severity, String message,
			int penalty) {
		alerts.add(new Alert(code, severity, message));
		impacts.add("-" + penalty + " : " + message);
	}
}
