package com.devices.app.controller.home;

import ch.qos.logback.classic.Logger;
import com.devices.app.dtos.dto.SkinTherapistDto;
import com.devices.app.services.SkinTherapistService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class TeamController {
    private final SkinTherapistService skinTherapistService;

    public TeamController(SkinTherapistService skinTherapistService) {
        this.skinTherapistService = skinTherapistService;
    }
    @GetMapping("/team")
    public String team(Model model) {
        List<SkinTherapistDto> list = skinTherapistService.getAllTherapists();
        model.addAttribute("skinTherapists", list);
        return "home/team";
    }

}
