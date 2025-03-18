package com.devices.app.controller.admin;

import com.devices.app.dtos.UserCreationRequest;
import com.devices.app.models.Users;
import com.devices.app.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class UserAdminController {
    @Autowired
    private UserService userService;

    @PostMapping("/users")
    Users CreateUser(@RequestBody UserCreationRequest request)
    {
        return userService.createRequest(request);
    }
}
