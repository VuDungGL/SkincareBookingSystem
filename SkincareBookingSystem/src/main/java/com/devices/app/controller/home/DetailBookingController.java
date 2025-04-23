package com.devices.app.controller.home;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DetailBookingController {
    @GetMapping("DetailBooking")
    public String detailBooking(Model model) {
        return "home/detailBooking";
    }
}
