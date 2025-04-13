package com.devices.app.controller.authentication;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class changeController {
    @GetMapping("/login/forgotPassword")
    public String forgotPassword() {
        return "login/change";
    }
}