package com.devices.app.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Integer userID;
    private String userName;
    private Integer roleID;
    private String email;
    private String phone;
    private String firstName;
    private String lastName;
    private String avt;
    private Integer gender;
    private OffsetDateTime birthDate;
    private Integer status;
}
