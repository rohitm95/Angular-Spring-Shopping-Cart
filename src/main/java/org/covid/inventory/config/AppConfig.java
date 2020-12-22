package org.covid.inventory.config;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@Configuration
public class AppConfig {

	private static final String BASE_NAME = "classpath:messages";
	private static final String ENCODING = "UTF-8";

	@Value("${messageSource.cache.time}")
	private Integer cacheSeconds;
	
	@Value("${google.clientId}")
	private String googleClientId;

	@Bean
	public MessageSource messageSource() {
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasename(BASE_NAME);
		messageSource.setDefaultEncoding(ENCODING);
		messageSource.setCacheSeconds(cacheSeconds);
		return messageSource;
	}

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}
	
	@Bean
    public LocalValidatorFactoryBean getValidator() {
        LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
        bean.setValidationMessageSource(messageSource());
        return bean;
    }
	
}
