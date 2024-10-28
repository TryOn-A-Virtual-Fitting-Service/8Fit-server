package com.example.webapplicationserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class WebApplicationServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebApplicationServerApplication.class, args);
	}

}
