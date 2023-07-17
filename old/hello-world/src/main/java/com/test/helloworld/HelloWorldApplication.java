package com.test.helloworld;

import java.util.TimeZone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.config.EnableWebFlux;

import jakarta.annotation.PostConstruct;






@SpringBootApplication
@EnableWebFlux
public class HelloWorldApplication {

	public static void main(String[] args) {
		SpringApplication.run(HelloWorldApplication.class, args);
	}
    @PostConstruct
    public void init(){ 
      // Setting Spring Boot SetTimeZone
      TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

}
