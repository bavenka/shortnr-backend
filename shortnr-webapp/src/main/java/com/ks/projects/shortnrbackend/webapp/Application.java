package com.ks.projects.shortnrbackend.webapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableAutoConfiguration
@ComponentScan({"com.ks.projects.shortnrbackend.services.services","com.ks.projects.shortnrbackend.webapp"})
@EntityScan("com.ks.projects.shortnrbackend.data.model")
@EnableJpaRepositories("com.ks.projects.shortnrbackend.data.repository")
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);

	}
}
