package com.devices.app.models;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "S_BookingDetail", schema = "dbo")
public class BookingDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Column(name = "BookingID", nullable = false)
    private Integer bookingID;

    @Column(name = "ServiceID", nullable = false)
    private Integer serviceID;

    @Column(name = "SpecialistID", nullable = false)
    private Integer specialistID;

    @Column(name = "Price", nullable = false, precision = 18, scale = 2)
    private BigDecimal price;

    @Column(name = "Status", nullable = false)
    private Integer status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getBookingID() {
        return bookingID;
    }

    public void setBookingID(Integer bookingID) {
        this.bookingID = bookingID;
    }

    public Integer getServiceID() {
        return serviceID;
    }

    public void setServiceID(Integer serviceID) {
        this.serviceID = serviceID;
    }

    public Integer getSpecialistID() {
        return specialistID;
    }

    public void setSpecialistID(Integer specialistID) {
        this.specialistID = specialistID;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

}