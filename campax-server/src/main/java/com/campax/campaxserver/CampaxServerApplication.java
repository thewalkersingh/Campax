package com.campax.campaxserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CampaxServerApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(CampaxServerApplication.class, args);
		System.out.println("+----------------------------------+");
		System.out.println("|      ^ ^                         |");
		System.out.println("|     (•_•)   App Started...!!     |");
		System.out.println("|                                  |");
		System.out.println("+----------------------------------+");
		
	}
	
}