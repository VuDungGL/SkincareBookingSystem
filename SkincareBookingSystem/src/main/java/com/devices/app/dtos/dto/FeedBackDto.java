package com.devices.app.dtos.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FeedBackDto {
    private Integer feedbackID;
    private Integer rating;
    private String comment;
    private OffsetDateTime createDate;
    private String fullName;
    private String avt;
    private Integer gender;
    private String serviceName;
}
