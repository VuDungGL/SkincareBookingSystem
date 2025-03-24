package com.devices.app.dtos;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

@Setter
@Getter
public class UserCreationRequest {
    private String userName;
    private Integer roleID;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String phone;

}
