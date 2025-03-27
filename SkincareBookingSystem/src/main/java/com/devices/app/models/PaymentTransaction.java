package com.devices.app.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

@Getter
@Setter
@Entity
@Table(name = "S_PaymentTransaction")
public class PaymentTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @NotNull
    @Column(name = "PaymentID", nullable = false)
    private Integer paymentID;

    @Size(max = 50)
    @NotNull
    @Nationalized
    @Column(name = "TransactionCode", nullable = false, length = 50)
    private String transactionCode;

    @Size(max = 20)
    @NotNull
    @Nationalized
    @Column(name = "TransactionStatus", nullable = false, length = 20)
    private String transactionStatus;

}