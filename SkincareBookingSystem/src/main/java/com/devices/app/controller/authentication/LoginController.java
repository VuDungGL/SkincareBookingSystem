package com.devices.app.controller.authentication;

import com.devices.app.config.jwt.AppProperties;
import com.devices.app.config.jwt.EmailSetting;
import com.devices.app.dtos.dto.AuthenticationDto;
import com.devices.app.dtos.dto.UserDto;
import com.devices.app.dtos.response.LoginInfo;
import com.devices.app.dtos.response.TokenInfo;
import com.devices.app.infrastructure.userEnum.UserRoleEnum;
import com.devices.app.models.Users;
import com.devices.app.services.JWTService;
import com.devices.app.services.UserService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;

@Controller
public class LoginController {
    private final UserService userService;
    private final JWTService jwtService;
    private final AppProperties appProperties;

    public LoginController(UserService userService, JWTService jwtService, AppProperties appProperties) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.appProperties = appProperties;
    }

    @GetMapping("login")
    public String showLoginPage(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated() && !(authentication.getPrincipal() instanceof String)) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            var authorities = userDetails.getAuthorities();
            if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN") || a.getAuthority().equals("ROLE_MASTER_ADMIN"))) {
                response.sendRedirect("/admin/dashboard");
                return null;
            } else {
                response.sendRedirect("/");
                return null;
            }
        }

        return "login/login";
    }

    @PostMapping("/login/onLogin")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request, HttpServletResponse response) {
        String userName = request.get("userName");
        String password = request.get("password");

        TokenInfo tokenInfo = userService.login(userName, password);
        String refreshToken = jwtService.refreshToken(tokenInfo.getToken());

        if (tokenInfo == null) {
            return ResponseEntity.badRequest().body("Tên đăng nhập hoặc mật khẩu không đúng!");
        }
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
        LoginInfo loginInfo = new LoginInfo(tokenInfo, refreshToken);

        return ResponseEntity.ok(loginInfo);
    }

    @PostMapping("/login/refresh")
    public ResponseEntity<?> refreshToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        String newAccessToken = jwtService.refreshToken(token);
        if (newAccessToken == null || newAccessToken.isEmpty()) {
            return ResponseEntity.badRequest().body("Refresh token không hợp lệ hoặc đã hết hạn!");
        }

        return ResponseEntity.ok(newAccessToken);
    }

    @PostMapping("/login/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        // Hủy cookie bằng cách set MaxAge = 0
        Cookie accessToken = new Cookie("access_token", null);
        accessToken.setHttpOnly(true);
        accessToken.setPath("/");
        accessToken.setMaxAge(0);

        Cookie refreshToken = new Cookie("refresh_token", null);
        refreshToken.setHttpOnly(true);
        refreshToken.setPath("/");
        refreshToken.setMaxAge(0);

        response.addCookie(accessToken);
        response.addCookie(refreshToken);

        return ResponseEntity.ok("Đăng xuất thành công");
    }

}
