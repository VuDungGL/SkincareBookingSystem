package com.devices.app.controller.authentication;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class demiController {
    @GetMapping("eror")
    public String demi() {
        return "login/demi";
    }
}
