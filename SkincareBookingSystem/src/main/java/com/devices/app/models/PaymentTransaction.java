package com.devices.app.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

@Setter
@Getter
@Entity
@Table(name = "S_PaymentTransaction")
public class PaymentTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Column(name = "PaymentID", nullable = false)
    private Integer paymentID;

    @Nationalized
    @Column(name = "TransactionCode", nullable = false, length = 50)
    private String transactionCode;

    @Nationalized
    @Column(name = "TransactionStatus", nullable = false, length = 20)
    private String transactionStatus;

}