package com.devices.app.dtos;

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
    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public Integer getServiceID() {
        return serviceID;
    }

    public void setServiceID(Integer serviceID) {
        this.serviceID = serviceID;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

}
