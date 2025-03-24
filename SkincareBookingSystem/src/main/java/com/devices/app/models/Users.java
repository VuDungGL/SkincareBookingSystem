package com.devices.app.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

@Setter
@Getter
@Entity
@Table(name = "S_Users")
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Nationalized
    @Column(name = "UserName", length = 20)
    private String userName;

    @Column(name = "RoleID", nullable = false)
    private Integer roleID;

    @Nationalized
    @Column(name = "Email", length = 50)
    private String email;

    @Nationalized
    @Lob
    @Column(name = "Password")
    private String password;

    @Nationalized
    @Column(name = "FirstName", length = 50)
    private String firstName;

    @Nationalized
    @Column(name = "LastName", length = 50)
    private String lastName;

    @Nationalized
    @Column(name = "Phone", length = 12)
    private String phone;

    @Column(name = "Flag", nullable = false)
    private Integer flag;

}