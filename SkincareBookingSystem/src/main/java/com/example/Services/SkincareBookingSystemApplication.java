package com.example.Services;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.example")
public class SkincareBookingSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(SkincareBookingSystemApplication.class, args);
	}

}