package com.devices.app.dtos.dto;

import com.devices.app.infrastructure.userEnum.UserRoleEnum;
import com.devices.app.models.Users;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Collections;

@Getter
public class CustomerUserDetails implements UserDetails {

    private final Users user;
    private final UserRoleEnum role;

    public CustomerUserDetails(Users user) {
        this.user = user;
        this.role = UserRoleEnum.fromValue(user.getRoleID());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getPassword() {
        return user.getPassword(); // Có thể null với master admin
    }

    @Override
    public String getUsername() {
        return user.getEmail(); // Hoặc userName nếu bạn dùng tên đăng nhập khác
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return user.getStatus() != 0; // ví dụ: 0 là bị khóa
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user.getStatus() == 1;
    }

    public UserRoleEnum getRole() {
        return UserRoleEnum.fromValue(user.getRoleID());
    }

}
