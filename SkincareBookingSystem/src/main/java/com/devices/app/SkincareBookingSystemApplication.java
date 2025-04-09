package com.devices.app;

import com.devices.app.config.HashUtil;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.devices")
public class SkincareBookingSystemApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(SkincareBookingSystemApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// Mã hóa mật khẩu khi ứng dụng khởi động
		String rawPassword = "hashed_admin02";
		String encodedPassword = HashUtil.encodePassword(rawPassword);

		String encodedPassword2 = HashUtil.encodePassword("hashed_member03");
		// In ra mật khẩu đã mã hóa
		System.out.println("Mã hóa mật khẩu: " + encodedPassword);
		System.out.println("Mã hóa mật khẩu: " + encodedPassword2);
	}
}
