package com.devices.app.dtos;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class SkinTherapistDto {
    private int skinTherapistID;
    private String email;
    private String firstName;
    private String lastName;
    private String phone;
    private String avt;
    private Double total;
    private int totalTask;
    private BigDecimal percentTask;
    private OffsetDateTime birthDate;
    private int gender;
    private String expertise;
    private int experience;
    private int salary;
    private int status;

    public SkinTherapistDto(int skinTherapistID, String email, String firstName, String lastName, String phone, OffsetDateTime birthDate, String avt, int gender, String expertise, int experience, int salary, int status) {
        this.skinTherapistID = skinTherapistID;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.avt = avt;
        this.birthDate = birthDate;
        this.gender = gender;
        this.expertise = expertise;
        this.experience = experience;
        this.salary = salary;
        this.status = status;
    }
}
