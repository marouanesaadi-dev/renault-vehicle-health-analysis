package ma.renault.analysis.config;

import ma.renault.analysis.domain.HealthScoreCalculator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AnalysisConfiguration {
	@Bean
	HealthScoreCalculator healthScoreCalculator() {
		return new HealthScoreCalculator();
	}
}
