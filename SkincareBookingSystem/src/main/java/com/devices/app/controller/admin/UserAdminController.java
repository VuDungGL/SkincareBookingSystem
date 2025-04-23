package com.devices.app.controller.admin;

import com.devices.app.dtos.dto.AnnualStatisticsDto;
import com.devices.app.dtos.dto.UserDto;
import com.devices.app.dtos.requests.UserUpdateRequest;
import com.devices.app.dtos.response.ApiResponse;
import com.devices.app.dtos.response.TokenInfo;
import com.devices.app.models.Users;
import com.devices.app.services.JWTService;
import com.devices.app.services.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.token.TokenService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
public class UserAdminController {
    private final UserService userService;
    private final JWTService jwtService;

    public UserAdminController(UserService userService, JWTService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MASTER_ADMIN')")
    @GetMapping("/users/TotalMember")
    long TotalMember(){
        return userService.GetTotalMember();
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MASTER_ADMIN')")
    @PostMapping("/users/annualNewMember")
    public List<AnnualStatisticsDto> annualNewMember(@RequestBody AnnualStatisticsDto request){
        return userService.AnnualNewMembers(request.getYear());
    }


    @PreAuthorize("hasRole('ADMIN') or hasRole('MASTER_ADMIN')")
    @PostMapping("/users/deleteUser")
    public ResponseEntity<String> deleteUser(@RequestBody Map<String, Integer> request) {
        int userID = request.get("userID");
        String result = userService.deleteUser(userID);
        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MASTER_ADMIN')")
    @PostMapping("/users/getListCustomer")
    public List<Users> getListCustomer(@RequestBody Map<String, Integer> request) {
        int status = request.get("status");
        return userService.getListCustomer(status);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MASTER_ADMIN')")
    @PostMapping("/users/getListCustomerByUserRole")
    public Page<UserDto> getListCustomerByUserRole(@RequestBody Map<String, Object> request) {
        int roleID = Integer.parseInt(request.get("roleID").toString());
        int pageIndex = (int) request.getOrDefault("pageIndex", 0);
        int pageSize = (int) request.getOrDefault("pageSize", 6);
        String searchText = (String) request.getOrDefault("searchText", "");

        if (pageIndex < 0) pageIndex = 0;
        if (pageSize <= 0) pageSize = 6;
        return userService.getListCustomerByUserRole(searchText,roleID,pageIndex,pageSize);
    }

}
