package com.devices.app.controller.home;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PriceController {

    @GetMapping("/price")
    public String about() {
        return "home/price";
    }
}
