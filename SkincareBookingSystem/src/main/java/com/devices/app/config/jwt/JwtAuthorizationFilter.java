package com.devices.app.config.jwt;

import com.devices.app.dtos.dto.CustomerUserDetails;
import com.devices.app.infrastructure.userEnum.UserRoleEnum;
import com.devices.app.services.JWTService;
import com.devices.app.services.UserService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Slf4j
@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    private final JWTService jwtService;
    private final  UserService customUserDetailsService;

    public JwtAuthorizationFilter(JWTService jwtService, UserService customUserDetailsService) {
        this.jwtService = jwtService;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String token = getTokenFromCookies(request, "access_token");

        if (token != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                Claims claims = jwtService.validateToken(token);
                String subject = claims.getSubject();
                if ("MASTER_ADMIN".equals(subject)) {
                    Integer roleValue = (Integer) claims.get("typ");
                    UserRoleEnum roleEnum = UserRoleEnum.fromValue(roleValue);
                    SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + roleEnum.name());
                    UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                            "masteradmin", "", Collections.singleton(authority)
                    );
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(userDetails, null, Collections.singleton(authority));
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else {
                    Long userId = Long.parseLong(subject);
                    CustomerUserDetails userDetails = (CustomerUserDetails) customUserDetailsService.loadUserById(userId);
                    if (userDetails != null) {
                        // ✅ Lấy role từ claims và convert sang enum
                        Integer roleValue = (Integer) claims.get("typ");
                        UserRoleEnum roleEnum = UserRoleEnum.fromValue(roleValue); // bạn có thể tạo fromValue trong enum

                        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + roleEnum.name());

                        UsernamePasswordAuthenticationToken authentication =
                                new UsernamePasswordAuthenticationToken(userDetails, null, Collections.singleton(authority));
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                }

            } catch (Exception ex) {
                log.error("❌ Token không hợp lệ: {}", ex.getMessage());
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Invalid Token");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private String getTokenFromCookies(HttpServletRequest request, String name) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals(name)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
