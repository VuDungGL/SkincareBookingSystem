package com.devices.app.controller;

import com.devices.app.dtos.dto.CustomerUserDetails;
import com.devices.app.dtos.requests.UserUpdateRequest;
import com.devices.app.dtos.response.ApiResponse;
import com.devices.app.dtos.response.TokenInfo;
import com.devices.app.models.Users;
import com.devices.app.repository.UserRepository;
import com.devices.app.services.JWTService;
import com.devices.app.services.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    private final UserService userService;
    private final JWTService jwtService;
    public UserController(UserService userService, JWTService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }


    @PostMapping("/users/updateInfo")
    @PreAuthorize("hasRole('USER') or hasRole('MASTER_ADMIN')")
    public ResponseEntity<ApiResponse<TokenInfo>> updateUser(@RequestBody UserUpdateRequest request,
                                                             @AuthenticationPrincipal CustomerUserDetails userDetails,
                                                             HttpServletResponse response) {
        Users user = userDetails.getUser();
        int userid = user.getId();
        ApiResponse<TokenInfo> responseApi = userService.updateUser(userid,request);
        TokenInfo tokenInfo = responseApi.getData();
        String refreshToken = jwtService.refreshToken(tokenInfo.getToken());

        Cookie accessCookie = new Cookie("access_token", tokenInfo.getToken());
        accessCookie.setHttpOnly(true); // không cho JS đọc
        accessCookie.setPath("/");
        accessCookie.setMaxAge(60 * 60); // 1h

        response.addCookie(accessCookie);

        // Đặt refresh_token nếu cần
        Cookie refreshCookie = new Cookie("refresh_token", refreshToken);
        refreshCookie.setHttpOnly(true);
        refreshCookie.setPath("/");
        refreshCookie.setMaxAge(7 * 24 * 60 * 60); // 7 ngày
        response.addCookie(refreshCookie);
        return new ResponseEntity<>(responseApi, HttpStatus.OK);
    }
}
