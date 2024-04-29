package com.asm.shopvista.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfiguration {

	private static final String OPTIONS = "OPTIONS";
	private static final String GET = "GET";
	private static final String POST = "POST";
	private static final String PUT = "PUT";
	private static final String DELETE = "DELETE";

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**").allowedMethods(GET, POST, PUT, DELETE, OPTIONS).allowedHeaders("*")
						.allowedOriginPatterns("*").allowedOrigins("http://localhost:4200").allowCredentials(true);
			}
		};
	}

	@Bean
	public DefaultSecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf(AbstractHttpConfigurer::disable).authorizeHttpRequests(auth -> {
			auth.requestMatchers("/api/v1/auth/**").permitAll();
			auth.requestMatchers("/api/v1/admin/**").hasRole("Admin");
			auth.requestMatchers("/api/v1/user/**").permitAll();
			auth.requestMatchers("/api/v1/products/all").permitAll();
			auth.requestMatchers("/api/v1/products/{productId}").permitAll();
			auth.requestMatchers("/api/v1/products/add").hasRole("Admin");
			auth.requestMatchers("/api/v1/cart/**").permitAll();
			auth.requestMatchers("/api/v1/payments/**").permitAll();
			auth.requestMatchers("/api/v1/orders/**").permitAll();
			auth.anyRequest().authenticated();
		});

		return http.build();
	}

}
