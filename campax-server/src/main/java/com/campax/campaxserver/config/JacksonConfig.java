package com.campax.campaxserver.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

//@Configuration
public class JacksonConfig {
	
	//	@Bean
	public ObjectMapper objectMapper() {
		ObjectMapper mapper = new ObjectMapper();
		// Register Java 8 date/time module if you use LocalDate, LocalDateTime, etc.
		mapper.registerModule(new JavaTimeModule());
		return mapper;
	}
	
}