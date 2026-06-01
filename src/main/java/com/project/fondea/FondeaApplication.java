package com.project.fondea;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FondeaApplication {

	public static void main(String[] args) {
		SpringApplication.run(FondeaApplication.class, args);
	}

}