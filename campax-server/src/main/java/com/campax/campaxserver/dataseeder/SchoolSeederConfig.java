package com.campax.campaxserver.dataseeder;

import com.campax.campaxserver.model.School;
import com.campax.campaxserver.repository.SchoolRepository;
import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SchoolSeederConfig {
	@Autowired
	SchoolRepository schoolRepository;
	
	private final Faker faker = new Faker();
	
	@Bean
	CommandLineRunner seedSchools(SchoolRepository schoolRepository) {
		return args -> {
			for (int i = 0; i < 10; i++) {
				School school = new School();
				school.setName(faker.university().name());
				school.setSubdomain(faker.internet().domainWord() + ".campax.com");
				school.setPlanTier(faker.options().option("FREE", "STANDARD", "PREMIUM"));
				school.setActive(true);
				
				schoolRepository.save(school);
			}
		};
	}
	
}