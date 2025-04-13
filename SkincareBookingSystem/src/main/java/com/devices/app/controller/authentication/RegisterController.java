package com.devices.app.controller.authentication;

import com.devices.app.dtos.requests.RegisterRequest;
import com.devices.app.dtos.response.ApiResponse;
import com.devices.app.models.Users;
import com.devices.app.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class RegisterController {
    private final UserService userService;

    public RegisterController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("register")
    public String register() {
        return "login/register";
    }

    @PostMapping("/register/onRegister")
    public ResponseEntity<ApiResponse<String>> register(@Valid @RequestBody RegisterRequest request) {
        ApiResponse<String> response = userService.register(request);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
