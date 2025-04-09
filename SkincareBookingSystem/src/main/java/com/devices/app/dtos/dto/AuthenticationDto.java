package com.devices.app.dtos.dto;

import com.devices.app.dtos.response.TokenInfo;
import com.devices.app.models.Users;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AuthenticationDto {
    private Users userInfo;
    private TokenInfo tokenInfo;
    private String errorMessage; // Thêm trường để lưu thông báo lỗi

    // Getters and Setters

    public AuthenticationDto(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public AuthenticationDto() {

    }
}
