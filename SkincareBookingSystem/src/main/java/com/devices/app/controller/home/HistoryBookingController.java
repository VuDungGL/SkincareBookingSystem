package com.devices.app.controller.home;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HistoryBookingController {
    @GetMapping("HistoryBooking")
    public String historyBooking() {
        return "home/historyBooking";
    }
}
