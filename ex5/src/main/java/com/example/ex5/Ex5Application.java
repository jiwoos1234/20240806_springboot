package com.example.ex5;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaAuditing //BasicEntity를 사용하기 위함.
@EnableJpaRepositories(basePackages = "com.example.ex5.repository")

public class Ex5Application {

	public static void main(String[] args) {
		SpringApplication.run(Ex5Application.class, args);
		System.out.println("http://localhost:8080/ex5");
	}

}
