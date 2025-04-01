package com.devices.app.controller.home;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestimonialController {
    @GetMapping("/testimonial")
    public String Testimonial() {
        return "home/testimonial";
    }
}
