package com.devices.app.dtos;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Date;

@Setter
@Getter
public class UserDto {
    private int userId;
    private String userName;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String address;
    private Date createDate;
    private String avt;
    private Double total;
    private int totalTask;
    private BigDecimal percentTask;

    public UserDto(int userId, String userName, String email, String firstName, String lastName, String phone, String avt, Double total, int totalTask, BigDecimal percentTask) {
        this.userId = userId;
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.avt = avt;
        this.total = total;
        this.totalTask = totalTask;
        this.percentTask = percentTask;
    }
}
