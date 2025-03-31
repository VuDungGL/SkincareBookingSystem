package com.devices.app.controller.home;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TeamController {
    @GetMapping("/team")
    public String team() {
        return "home/team";
    }
}
