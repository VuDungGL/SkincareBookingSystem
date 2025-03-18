package com.devices.app.controller.admin;

import com.devices.app.dtos.UserCreationRequest;
import com.devices.app.models.Users;
import com.devices.app.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserAdminController {
    @Autowired
    private UserService userService;

    @PostMapping("/users")
    Users CreateUser(@RequestBody UserCreationRequest request)
    {
        return userService.createRequest(request);
    }

    @GetMapping("/users/TotalMember")
    long TotalMember(){
        return userService.GetTotalMember();
    }
}
