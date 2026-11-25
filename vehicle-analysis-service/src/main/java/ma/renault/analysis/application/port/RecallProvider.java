package ma.renault.analysis.application.port;

public interface RecallProvider {
	boolean hasActiveRecall(String vin);
}
