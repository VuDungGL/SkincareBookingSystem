package com.devices.app.dtos;

import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Setter
@Getter
public class UserCreationRequest {
    @NotNull(message = "RoleID không được để trống")
    private Integer userID;

    @NotBlank(message = "UserName không được để trống")
    @Size(max = 20, message = "UserName không được quá 20 ký tự")
    @Pattern(regexp = "^[a-zA-Z0-9_.]+$", message = "UserName không được chứa ký tự đặc biệt")
    private String userName;

    @NotNull(message = "RoleID không được để trống")
    private Integer roleID;

    @NotBlank(message = "Email không được để trống")
    @Size(max = 50, message = "Email không được quá 50 ký tự")
    private String email;

    @NotBlank(message = "Mật khẩu không được để trống")
    private String password;

    @Size(max = 50, message = "FirstName không được quá 50 ký tự")
    private String firstName;

    @Size(max = 50, message = "LastName không được quá 50 ký tự")
    private String lastName;

    @Size(max = 12, message = "Số điện thoại không được quá 12 ký tự")
    private String phone;

    @NotNull(message = "Status không được để trống")
    private Integer status;

    private String avt; // Ảnh đại diện có thể null

    private Integer gender;

    private LocalDateTime birthDay;
}
