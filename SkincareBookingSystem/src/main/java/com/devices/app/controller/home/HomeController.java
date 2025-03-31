package com.devices.app.controller.home;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/")
    public String index() {
        return "home/index"; // Tên file trong templates/
    }

    @GetMapping ("/home")
    public String home() {
        return "home/index"; // Tên file trong templates/
    }

}