package com.devices.app.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateRequest {
    private Integer userID;
    private String userName;
    private Integer roleID;
    private String email;
    private String phone;
    private String firstName;
    private String lastName;
    private String avt;
    private Integer gender;
    private Instant birthDay;
    private Integer status;
    private MultipartFile avatar;
}
