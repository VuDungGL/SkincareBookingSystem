package com.devices.app.controller.home;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class InfoUserController {
    @GetMapping("/infoUser")
    public String infoUser() {
        return "home/InfoUser";
    }
}
