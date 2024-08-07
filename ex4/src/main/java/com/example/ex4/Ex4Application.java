package com.example.ex4;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaAuditing //BasicEntity를 사용하기 위함.
@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.example.ex4.repository")
		public class Ex4Application {

	public static void main(String[] args) {
		SpringApplication.run(Ex4Application.class, args);
		System.out.println("http://localhost:8080/ex4");
	}

}
