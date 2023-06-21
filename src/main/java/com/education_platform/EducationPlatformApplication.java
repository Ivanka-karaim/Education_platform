package com.education_platform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
public class EducationPlatformApplication {

	public static void main(String[] args) {
		SpringApplication.run(EducationPlatformApplication.class, args);
		System.out.println("Hi");
	}

}
