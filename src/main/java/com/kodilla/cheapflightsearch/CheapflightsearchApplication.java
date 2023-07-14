package com.kodilla.cheapflightsearch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class CheapflightsearchApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(CheapflightsearchApplication.class, args);
	}
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(CheapflightsearchApplication.class);
	}
}
