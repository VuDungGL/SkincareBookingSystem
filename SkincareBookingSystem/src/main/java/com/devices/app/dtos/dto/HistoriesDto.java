package com.devices.app.dtos.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HistoriesDto {
    private Integer bookingDetailID;
    private String email;
    private String fullName;
    private String phone;
    private OffsetDateTime createDate;
    private Integer skinTherapistID;
    private Integer serviceID;
    private Double price;
    private Boolean isPaid;
    private Integer status;
    private LocalDate workDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String therapistFirstName;
    private String therapistLastName;
    private String serviceName;
}
