package org.covid.inventory.config;

import org.covid.inventory.exceptions.InventoryAuthenticationFailureHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	private static final String IMAGES = "/assets/**";

	private static final String MANIFEST_JSON = "/manifest.json";

	private static final String STYLES_STYLE_CSS = "/styles/style.css";
	
	private static final String INDEX = "/index.html";

	@Autowired
	private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

	@Autowired
	private UserDetailsService jwtUserDetailsService;

	@Autowired
	private JwtRequestFilter jwtRequestFilter;

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {

		auth.userDetailsService(jwtUserDetailsService).passwordEncoder(passwordEncoder());
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		// TODO Auto-generated method stub
		super.configure(web);
		web.ignoring().antMatchers("/favicon.ico", "*.css");

	}

	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.headers().frameOptions().disable();
		// We don't need CSRF for this example
		httpSecurity.csrf().disable()
		// dont authenticate this particular request
		.authorizeRequests()
		
		/*
		 .antMatchers("/admin/**").hasIpAddress("127.0.0.1")
		.antMatchers(Constants.AUTHENTICATE_PATTERN, Constants.HEALTH_CHECK_PATTERN, STYLES_STYLE_CSS,
				MANIFEST_JSON, IMAGES, INDEX, "/built/bundle.js", "/built/bundle.js.map", "/signin", "/dashboard","/h2-console/**")*/
		
		// all requests starts with "/api" need to be permit authenticated
		//.antMatchers("/api/**")
	//	.authenticated()
		// all other requests need to be permit all
		.anyRequest()
		.permitAll()
		.and()
		.formLogin()
		.failureHandler(inventoryAuthenticationFailureHandler())
		.and()
		// make sure we use stateless session; session won't be used to
		// store user's state.
		.exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint).and()
		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED);
		// Add a filter to validate the tokens with every request
		httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
		
	}

	@Bean
	public ObjectMapper objectMapper() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.addMixIn(Object.class, IgnoreHibernatePropertiesInJackson.class);
		mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		return mapper;
	}

	@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
	private abstract class IgnoreHibernatePropertiesInJackson {
	}

	@Bean
	public AuthenticationFailureHandler inventoryAuthenticationFailureHandler() {
		return new InventoryAuthenticationFailureHandler();
	}
}