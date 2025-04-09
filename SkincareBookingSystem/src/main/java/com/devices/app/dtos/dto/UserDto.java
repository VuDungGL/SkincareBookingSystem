package com.devices.app.dtos.dto;

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
    private String password;

    public UserDto(Integer userID, String userName, Integer roleID, String email, String phone, String firstName, String lastName, String avt, Integer gender, OffsetDateTime birthDate, Integer status) {
        this.userID = userID;
        this.userName = userName;
        this.roleID = roleID;
        this.email = email;
        this.phone = phone;
        this.firstName = firstName;
        this.lastName = lastName;
        this.avt = avt;
        this.gender = gender;
        this.birthDate = birthDate;
        this.status = status;
    }
}
