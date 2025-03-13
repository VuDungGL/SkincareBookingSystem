package com.example.Models;

import jakarta.persistence.*;
import org.hibernate.annotations.Nationalized;

import java.math.BigDecimal;

@Entity
@Table(name = "S_BookingDetail")
public class BookingDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "BookingID", nullable = false)
    private Booking bookingID;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ServiceID", nullable = false)
    private Service serviceID;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "SpecialistID", nullable = false)
    private Users specialistID;

    @Column(name = "Price", nullable = false, precision = 18, scale = 2)
    private BigDecimal price;

    @Nationalized
    @Column(name = "Status", nullable = false, length = 20)
    private String status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Booking getBookingID() {
        return bookingID;
    }

    public void setBookingID(Booking bookingID) {
        this.bookingID = bookingID;
    }

    public Service getServiceID() {
        return serviceID;
    }

    public void setServiceID(Service serviceID) {
        this.serviceID = serviceID;
    }

    public Users getSpecialistID() {
        return specialistID;
    }

    public void setSpecialistID(Users specialistID) {
        this.specialistID = specialistID;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}