package com.devices.app.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "S_Payment")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @NotNull
    @Column(name = "BookingID", nullable = false)
    private Integer bookingID;

    @NotNull
    @Column(name = "Amount", nullable = false, precision = 18, scale = 2)
    private BigDecimal amount;

    @Size(max = 20)
    @NotNull
    @Nationalized
    @Column(name = "PaymentMethod", nullable = false, length = 20)
    private String paymentMethod;

    @Size(max = 20)
    @NotNull
    @Nationalized
    @Column(name = "PaymentStatus", nullable = false, length = 20)
    private String paymentStatus;

    @ColumnDefault("getdate()")
    @Column(name = "TransactionDate")
    private Instant transactionDate;

}