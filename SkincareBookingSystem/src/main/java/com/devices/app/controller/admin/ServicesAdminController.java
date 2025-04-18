package com.devices.app.controller.admin;

import com.devices.app.dtos.requests.ServiceRequest;
import com.devices.app.dtos.response.ApiResponse;
import com.devices.app.models.Services;
import com.devices.app.services.ServicesService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
public class ServicesAdminController {
    private final ServicesService servicesService;

    public ServicesAdminController(ServicesService servicesService) {
        this.servicesService = servicesService;
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MASTER_ADMIN')")
    @GetMapping("/services/getAll")
    public ResponseEntity<ApiResponse<List<Services>>> getAll() {
        ApiResponse<List<Services>> response = servicesService.findAll();
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MASTER_ADMIN')")
    @PostMapping("/services/deleteService")
    public ResponseEntity<?> deleteService(@RequestBody Map<String, Integer> request) {
        Integer serviceID = request.get("serviceID");
        ApiResponse response = servicesService.deleteByID(serviceID);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MASTER_ADMIN')")
    @PostMapping("/services/createOrUpdateService")
    public ResponseEntity<?> createOrUpdateService(@ModelAttribute ServiceRequest serviceRequest) {
        ApiResponse<Services> response = servicesService.createOrUpdate(serviceRequest);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MASTER_ADMIN')")
    @PostMapping("/services/getByID")
    public ResponseEntity<?> getByID(@RequestBody Map<String, Integer> request) {
        Integer serviceID = request.get("serviceID");
        ApiResponse<Services> response = servicesService.findById(serviceID);
        return ResponseEntity.ok(response);
    }
}
