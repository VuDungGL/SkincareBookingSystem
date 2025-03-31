package com.devices.app.controller.home;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class contactController {
    @GetMapping("/contact")
    public String contact() {
        return "home/contact";
    }
}
