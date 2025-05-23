package com.devices.app.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Table(name = "S_Booking")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Column(name = "UserID")
    private Integer userID;

    @Column(name = "BookingDate")
    private OffsetDateTime bookingDate;

    @Column(name = "BookingDetailID")
    private Integer bookingDetailID;

}