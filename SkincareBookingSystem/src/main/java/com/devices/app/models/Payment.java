package com.devices.app.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;

import java.math.BigDecimal;
import java.time.Instant;

@Setter
@Getter
@Entity
@Table(name = "S_Payment")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Column(name = "BookingID", nullable = false)
    private Integer bookingID;

    @Column(name = "Amount", nullable = false, precision = 18, scale = 2)
    private BigDecimal amount;

    @Nationalized
    @Column(name = "PaymentMethod", nullable = false, length = 20)
    private String paymentMethod;

    @Nationalized
    @Column(name = "PaymentStatus", nullable = false, length = 20)
    private String paymentStatus;

    @ColumnDefault("getdate()")
    @Column(name = "TransactionDate")
    private Instant transactionDate;

}