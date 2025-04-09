package com.devices.app.config;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class HashUtil {
    private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // Mã hóa mật khẩu
    public static String encodePassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    // Kiểm tra mật khẩu với BCrypt
    public static boolean checkPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
