package com.devices.app.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "S_BookingDetail")
public class BookingDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Column(name = "BookingID")
    private Integer bookingID;

    @Column(name = "ServiceID")
    private Integer serviceID;

    @Column(name = "StaffID")
    private Integer staffID;

    @Column(name = "Price")
    private Double price;

    @Column(name = "Status")
    private Integer status;

}