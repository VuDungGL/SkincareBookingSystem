package com.devices.app.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StaffInfoDto {
    private int userID;
    private String userName;
    private int roleID;
    private String email;
    private String firstName;
    private String lastName;
    private String phone;
    private int userStatus;
    private OffsetDateTime birthDay;
    private OffsetDateTime userCreateDate;
    private String avt;
    private int gender;

    private String department;
    private String expertise;
    private int experience;
    private int salary;
    private int staffStatus;
    private OffsetDateTime staffCreateDate;
    private String bankAccount;
    private String bankName;
    private String position;
}