package com.devices.app.controller.admin;

import com.devices.app.dtos.AnnualStatisticsDto;
import com.devices.app.dtos.DepartmentDto;
import com.devices.app.dtos.StaffInfoDto;
import com.devices.app.dtos.UserCreationRequest;
import com.devices.app.models.Users;
import com.devices.app.services.DepartmentService;
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

    @Autowired
    private DepartmentService departmentService;

    @PostMapping("/users")
    Users CreateUser(@RequestBody UserCreationRequest request)
    {
        return userService.createRequest(request);
    }

    @GetMapping("/users/TotalMember")
    long TotalMember(){
        return userService.GetTotalMember();
    }

    @PostMapping("/users/annualNewMember")
    public List<AnnualStatisticsDto> annualNewMember(@RequestBody AnnualStatisticsDto request){
        return userService.AnnualNewMembers(request.getYear());
    }

    @PostMapping("/users/getListStaff")
    public Page<StaffInfoDto> getListStaff(@RequestBody Map<String, Object> request) {
        int pageIndex = (int) request.getOrDefault("pageIndex", 0);
        int pageSize = (int) request.getOrDefault("pageSize", 6);
        String searchText = (String) request.getOrDefault("searchText", "");

        if (pageIndex < 0) pageIndex = 0;
        if (pageSize <= 0) pageSize = 6;
        return userService.getListStaff(searchText,pageIndex,pageSize);
    }
    @GetMapping("/users/getListDepartment")
    public List<DepartmentDto> getListDepartment() {
        return departmentService.getDepartmentList();
    }

    @PostMapping("/users/deleteStaff")
    public ResponseEntity<String> deleteStaff(@RequestBody Map<String, Integer> request) {
        int staffID = request.get("staffID");
        String result = userService.deleteStaff(staffID);
        return ResponseEntity.ok(result);
    }
}
