package com.devices.app.controller.home;

import com.devices.app.dtos.response.ApiResponse;
import com.devices.app.models.Services;
import com.devices.app.services.ServicesService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class ServiceController {
    private final ServicesService servicesService;

    public ServiceController(ServicesService servicesService) {
        this.servicesService = servicesService;
    }

    @GetMapping("/service")
    public String service(Model model) {
        ApiResponse<List<Services>> service = servicesService.findAll();
        model.addAttribute("services", service.getData());
        return "home/service";
    }
}
