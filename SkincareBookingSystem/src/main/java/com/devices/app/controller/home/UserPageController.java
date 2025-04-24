package com.devices.app.controller.home;

import com.devices.app.dtos.response.ApiResponse;
import com.devices.app.models.Services;
import com.devices.app.services.ServicesService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserPageController {
    private final ServicesService servicesService;

    public UserPageController(ServicesService servicesService) {
        this.servicesService = servicesService;
    }

    @GetMapping("/home/getAllServices")
    public ApiResponse<List<Services>> getAllServices() {
        return servicesService.findAll();
    }
}
