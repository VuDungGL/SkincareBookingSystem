package com.devices.app.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Cung cấp quyền truy cập tới thư mục uploads
        registry.addResourceHandler("/Uploads/**")
                .addResourceLocations("file:/E:/SkincareBookingSystem/SkincareBookingSystem/Uploads/"); // Thay đổi đường dẫn này thành đường dẫn thực tế tới thư mục uploads trên hệ thống của bạn
    }
}