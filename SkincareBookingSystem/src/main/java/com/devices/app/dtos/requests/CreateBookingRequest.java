package com.devices.app.dtos.requests;

import lombok.*;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.Date;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateBookingRequest {
    private OffsetDateTime createDate;
    private String fullName;
    private String phone;
    private String email;
    private String note;
    private LocalDate workDate;
    private LocalTime startTime;
    private Integer serviceID;
    private Integer userID;
    private Integer therapistID = null;
    private double price;
}
