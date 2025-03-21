package com.devices.app.dtos;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BookingDto {
    private String bookingId;
    private Integer serviceID;
    private String serviceName;
    private Long total;


    public BookingDto(Integer serviceID, String serviceName, Long total) {
        this.serviceID = serviceID;
        this.serviceName = serviceName;
        this.total = total;
    }
    public BookingDto() {  // Bổ sung constructor mặc định
    }

}
