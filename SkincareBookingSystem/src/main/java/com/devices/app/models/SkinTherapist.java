package com.devices.app.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Table(name = "S_SkinTherapist")
public class SkinTherapist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Size(max = 50)
    @Nationalized
    @Column(name = "FirstName", length = 50)
    private String firstName;

    @Size(max = 50)
    @Nationalized
    @Column(name = "LastName", length = 50)
    private String lastName;

    @Nationalized
    @Lob
    @Column(name = "Email")
    private String email;

    @Size(max = 12)
    @Nationalized
    @Column(name = "Phone", length = 12)
    private String phone;

    @Column(name = "BirthDate")
    private OffsetDateTime birthDate;

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
    @Column(name = "Avt")
    private String avt;

    @Column(name = "Gender")
    private Integer gender;

}