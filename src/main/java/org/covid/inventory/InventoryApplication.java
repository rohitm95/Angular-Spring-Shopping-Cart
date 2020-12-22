package org.covid.inventory;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.covid.inventory.audit.SpringSecurityAuditorAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
@PropertySource("classpath:storeInfo.properties")
public class InventoryApplication extends SpringBootServletInitializer implements CommandLineRunner {

	@Autowired
	private DataSource dataSource;

	private static final Logger logger = LoggerFactory.getLogger(InventoryApplication.class);

	@Bean
    public AuditorAware<String> auditorAware() {
        return new SpringSecurityAuditorAware();
    }

	
	public static void main(String[] args) {
		SpringApplication.run(InventoryApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(InventoryApplication.class);
	}

	@Override
	public void run(String... args) throws Exception {
		logger.debug("Max Idle: " + dataSource.getClass().getName());
		logger.debug("Max Idle: " + dataSource.getMaxIdle());
		logger.debug("Max Active: " + dataSource.getMaxIdle());
		logger.debug("Active: " + dataSource.getNumActive());
		logger.debug("Idle: " + dataSource.getNumIdle());
	}

}
