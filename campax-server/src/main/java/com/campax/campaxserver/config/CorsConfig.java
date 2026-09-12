package com.campax.campaxserver.config;

import org.jspecify.annotations.NonNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Dev-time CORS: allows the Vite dev server (default port 5173) to call
 * the API directly from the browser. Auth is deferred, so there's no
 * origin restriction beyond this yet - tighten this (and drive allowed
 * origins from configuration per environment, not a hardcoded literal)
 * once auth and a real deployment target exist.
 */
@Configuration
public class CorsConfig {
	
	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(@NonNull CorsRegistry registry) {
				registry.addMapping("/api/**")
						  .allowedOrigins("http://localhost:5173")
						  .allowedMethods("GET", "POST", "PATCH", "PUT", "DELETE", "OPTIONS")
						  .allowedHeaders("*");
			}
		};
	}
	
}