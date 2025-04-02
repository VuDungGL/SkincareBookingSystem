package com.devices.app.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.time.Instant;
import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Table(name = "S_Users")
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Size(max = 20)
    @Nationalized
    @Column(name = "UserName", length = 20)
    private String userName;

    @NotNull
    @Column(name = "RoleID", nullable = false)
    private Integer roleID;

    @Size(max = 50)
    @Nationalized
    @Column(name = "Email", length = 50)
    private String email;

    @Nationalized
    @Lob
    @Column(name = "Password")
    private String password;

    @Size(max = 50)
    @Nationalized
    @Column(name = "FirstName", length = 50)
    private String firstName;

    @Size(max = 50)
    @Nationalized
    @Column(name = "LastName", length = 50)
    private String lastName;

    @Size(max = 12)
    @Nationalized
    @Column(name = "Phone", length = 12)
    private String phone;

    @NotNull
    @Column(name = "Status", nullable = false)
    private Integer status;

    @Column(name = "CreateDate")
    private OffsetDateTime createDate;

    @Nationalized
    @Lob
    @Column(name = "Avt")
    private String avt;

    @Column(name = "Gender")
    private Integer gender;

    @Column(name = "BirthDay")
    private Instant birthDay;

}