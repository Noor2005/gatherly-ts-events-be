package com.techsisters.gatherly;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling  // Add this annotation
@EnableAsync
public class TSEventsPortalApplication {

	public static void main(String[] args) {
		SpringApplication.run(TSEventsPortalApplication.class, args);
	}

}
