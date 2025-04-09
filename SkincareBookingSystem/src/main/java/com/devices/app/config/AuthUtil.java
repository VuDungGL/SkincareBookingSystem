package com.devices.app.config;

import com.devices.app.dtos.dto.CustomerUserDetails;
import com.devices.app.infrastructure.userEnum.UserRoleEnum;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component("authUtil")
public class AuthUtil {
    public boolean hasRole(Authentication authentication, UserRoleEnum role) {
        if (authentication == null || !authentication.isAuthenticated()) return false;
        Object principal = authentication.getPrincipal();
        if (principal instanceof CustomerUserDetails userDetails) {
            return userDetails.getRole() == role;
        }
        return false;
    }

    public boolean hasAnyRole(Authentication authentication, UserRoleEnum... roles) {
        if (authentication == null || !authentication.isAuthenticated()) return false;
        Object principal = authentication.getPrincipal();
        if (principal instanceof CustomerUserDetails userDetails) {
            for (UserRoleEnum role : roles) {
                if (userDetails.getRole() == role) return true;
            }
        }
        return false;
    }
}
