package com.devices.app.controller.home;

import com.devices.app.dtos.dto.SkinTherapistDto;
import com.devices.app.dtos.response.ApiResponse;
import com.devices.app.models.Services;
import com.devices.app.models.SkinTherapist;
import com.devices.app.services.ServicesService;
import com.devices.app.services.SkinTherapistService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Controller
public class AppointmentController {
    private final ServicesService servicesService;
    private final SkinTherapistService skinTherapistService;

    public AppointmentController(ServicesService servicesService, SkinTherapistService skinTherapistService) {
        this.servicesService = servicesService;
        this.skinTherapistService = skinTherapistService;
    }

    @GetMapping("/appointment")
    public String appointment(Model model) {
        ApiResponse<List<Services>> response = servicesService.findAll();
        model.addAttribute("services", response.getData());
        return "home/appointment";
    }

    @GetMapping("/chooseTherapist")
    public String chooseTherapist(@RequestParam LocalDate workDate,
                                  @RequestParam LocalTime startTime,
                                  @RequestParam int serviceID,
                                  Model model) {
        int duration = servicesService.findById(serviceID).getData().getDuration();
        List<SkinTherapist> list = skinTherapistService.getListSkinTherapistNotBusy(workDate,startTime,startTime.plusMinutes(duration));

        model.addAttribute("skinTherapists", list);

        return "home/chooseTherapist";
    }


}
