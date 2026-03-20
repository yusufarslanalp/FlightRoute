package com.example.FlightRoute;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class FlightRouteApplication {

	public static void main(String[] args) {
		SpringApplication.run(FlightRouteApplication.class, args);
	}

}
