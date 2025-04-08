package com.devices.app.controller.login;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class demiController {
    @GetMapping("demi")
    public String demi() {
        return "login/demi";
    }
}
