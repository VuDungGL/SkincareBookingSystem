package com.devices.app.controller.home;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ServiceController {
    @GetMapping("/service")
    public String service() {
        return "home/service";
    }
}
