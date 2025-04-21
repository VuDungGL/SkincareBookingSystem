package com.devices.app.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/Uploads/**")
                .addResourceLocations("file:/E:/SkincareBookingSystem/SkincareBookingSystem/Uploads/")
                .addResourceLocations("file:/D:/SkincareBookingSystem/SkincareBookingSystem/Uploads/")
                .addResourceLocations("file:/C:/SkincareBookingSystem/SkincareBookingSystem/Uploads/")
                .addResourceLocations("file:/C:/Users/Admin/Desktop/gitclone/SkincareBookingSystem/SkincareBookingSystem/Uploads/");

    }
}