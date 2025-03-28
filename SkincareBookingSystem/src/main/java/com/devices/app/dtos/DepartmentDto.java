package com.devices.app.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentDto {
    private int departmentID;
    private String department;
    private int totalMember;
    private String icon;
    private int managerID;
    private String managerAvt;
    private String managerFirstName;
    private String managerLastName;
    private List<String> memberAvatars;
}
