package com.devices.app.controller.login;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RegisterController {
    @GetMapping("register")
    public String register() {
        return "login/register";
    }
}
