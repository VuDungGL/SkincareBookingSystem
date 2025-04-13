package com.devices.app.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    @NotBlank(message = "UserName không được để trống")
    @Size(max = 20, message = "UserName không được quá 20 ký tự")
    @Pattern(regexp = "^[a-zA-Z0-9_.]+$", message = "UserName không được chứa ký tự đặc biệt")
    private String userName;

    @NotBlank(message = "Email không được để trống")
    @Size(max = 50, message = "Email không được quá 50 ký tự")
    private String email;

    @NotBlank(message = "Mật khẩu không được để trống")
    private String password;

}
