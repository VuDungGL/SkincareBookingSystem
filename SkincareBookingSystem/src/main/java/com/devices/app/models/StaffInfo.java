package com.devices.app.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "S_StaffInfo")
public class StaffInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Column(name = "StaffID")
    private Integer staffID;

    @Column(name = "Department")
    private Integer department;

    @Nationalized
    @Lob
    @Column(name = "Expertise")
    private String expertise;

    @Column(name = "Experience")
    private Integer experience;

    @Column(name = "Salary")
    private Integer salary;

    @Column(name = "Status")
    private Integer status;

    @Column(name = "CreateDate")
    private LocalDate createDate;

    @Nationalized
    @Lob
    @Column(name = "BankAccount")
    private String bankAccount;

    @Nationalized
    @Lob
    @Column(name = "BankName")
    private String bankName;

    @Size(max = 50)
    @Nationalized
    @Column(name = "\"Position\"", length = 50)
    private String position;

}