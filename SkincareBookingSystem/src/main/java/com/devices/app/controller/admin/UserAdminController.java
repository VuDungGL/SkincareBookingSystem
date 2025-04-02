package com.devices.app.controller.admin;

import com.devices.app.dtos.AnnualStatisticsDto;
import com.devices.app.dtos.SkinTherapistDto;
import com.devices.app.models.SkinTherapist;
import com.devices.app.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class UserAdminController {
    @Autowired
    private UserService userService;


    @GetMapping("/users/TotalMember")
    long TotalMember(){
        return userService.GetTotalMember();
    }

    @PostMapping("/users/annualNewMember")
    public List<AnnualStatisticsDto> annualNewMember(@RequestBody AnnualStatisticsDto request){
        return userService.AnnualNewMembers(request.getYear());
    }

    @PostMapping("/users/getListSkinTherapist")
    public Page<SkinTherapistDto> getListSkinTherapist(@RequestBody Map<String, Object> request) {
        int pageIndex = (int) request.getOrDefault("pageIndex", 0);
        int pageSize = (int) request.getOrDefault("pageSize", 6);
        String searchText = (String) request.getOrDefault("searchText", "");

        if (pageIndex < 0) pageIndex = 0;
        if (pageSize <= 0) pageSize = 6;
        return userService.getListSkinTherapist(searchText,pageIndex,pageSize);
    }

    @PostMapping("/users/deleteUser")
    public ResponseEntity<String> deleteStaff(@RequestBody Map<String, Integer> request) {
        int userID = request.get("userID");
        String result = userService.deleteUser(userID);
        return ResponseEntity.ok(result);
    }
}
