package br.com.lucasbdourado.electronictimemarking.infrastructure.configuration;

import br.com.lucasbdourado.electronictimemarking.domain.service.WorkDayCalculator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationBeanConfig
{
	@Bean
	public WorkDayCalculator workDayCalculator()
	{
		return new WorkDayCalculator();
	}
}
