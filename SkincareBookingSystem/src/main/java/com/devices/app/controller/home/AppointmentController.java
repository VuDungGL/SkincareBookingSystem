package com.devices.app.controller.home;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AppointmentController {
    @GetMapping("/appointment")
    public String appointment() {
        return "home/appointment";
    }
}
