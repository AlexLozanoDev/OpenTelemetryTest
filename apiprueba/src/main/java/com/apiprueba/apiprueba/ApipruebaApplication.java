package com.apiprueba.apiprueba;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class ApipruebaApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApipruebaApplication.class, args);
	}

	@Bean
    RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder){return restTemplateBuilder.build();}

}


