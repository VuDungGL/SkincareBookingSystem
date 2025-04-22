package com.devices.app;

import com.devices.app.config.HashUtil;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.devices")
public class SkincareBookingSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(SkincareBookingSystemApplication.class, args);
	}


}
