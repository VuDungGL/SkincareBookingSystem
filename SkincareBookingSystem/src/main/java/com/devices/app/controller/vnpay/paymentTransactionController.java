package com.devices.app.controller.vnpay;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class paymentTransactionController {
    @GetMapping("/paymentTransaction")
    public String paymentTransaction() {
        return "paymentResult/plan";
    }
}
