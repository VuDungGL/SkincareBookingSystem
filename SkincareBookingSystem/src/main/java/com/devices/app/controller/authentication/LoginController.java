package com.devices.app.controller.authentication;

import com.devices.app.config.jwt.AppProperties;
import com.devices.app.dtos.response.ApiResponse;
import com.devices.app.dtos.response.LoginInfo;
import com.devices.app.dtos.response.TokenInfo;
import com.devices.app.services.JWTService;
import com.devices.app.services.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
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

        ApiResponse<TokenInfo> tokenInfo = userService.login(userName, password);

        if (tokenInfo == null || tokenInfo.getData() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse<>(401, tokenInfo.getMessage(), null));
        }

        TokenInfo token = tokenInfo.getData();
        String refreshToken = jwtService.refreshToken(token.getToken());

        // Set access_token cookie
        Cookie accessCookie = new Cookie("access_token", token.getToken());
        accessCookie.setHttpOnly(true);
        accessCookie.setPath("/");
        accessCookie.setMaxAge(60 * 60); // 1h
        response.addCookie(accessCookie);

        // Set refresh_token cookie
        Cookie refreshCookie = new Cookie("refresh_token", refreshToken);
        refreshCookie.setHttpOnly(true);
        refreshCookie.setPath("/");
        refreshCookie.setMaxAge(7 * 24 * 60 * 60); // 7 ngày
        response.addCookie(refreshCookie);

        // Trả về thông tin để frontend xử lý
        LoginInfo loginInfo = new LoginInfo(token, refreshToken);
        return ResponseEntity.status(tokenInfo.getStatus()).body(loginInfo);
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
