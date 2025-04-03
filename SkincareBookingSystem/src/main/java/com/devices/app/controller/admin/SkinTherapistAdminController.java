package com.devices.app.controller.admin;

import com.devices.app.dtos.SkinTherapistDto;
import com.devices.app.models.SkinTherapist;
import com.devices.app.services.SkinTherapistService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class SkinTherapistAdminController {
    final private SkinTherapistService skinTherapistService;

    public SkinTherapistAdminController(SkinTherapistService skinTherapistService) {
        this.skinTherapistService = skinTherapistService;
    }

    @PostMapping("/therapist/getListSkinTherapist")
    public Page<SkinTherapistDto> getListSkinTherapist(@RequestBody Map<String, Object> request) {
        int pageIndex = (int) request.getOrDefault("pageIndex", 0);
        int pageSize = (int) request.getOrDefault("pageSize", 6);
        String searchText = (String) request.getOrDefault("searchText", "");

        if (pageIndex < 0) pageIndex = 0;
        if (pageSize <= 0) pageSize = 6;
        return skinTherapistService.getListSkinTherapist(searchText,pageIndex,pageSize);
    }
    @GetMapping("/therapist/getAllSkinTherapist")
    public List<SkinTherapist> getAllSkinTherapist() {
        return skinTherapistService.getAllSkinTherapist();
    }

}
