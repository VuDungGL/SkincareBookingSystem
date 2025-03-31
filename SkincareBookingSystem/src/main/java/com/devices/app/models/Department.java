package com.devices.app.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

@Getter
@Setter
@Entity
@Table(name = "S_Department")
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Nationalized
    @Lob
    @Column(name = "Department")
    private String department;

    @Nationalized
    @Lob
    @Column(name = "Icon")
    private String icon;

    @Column(name = "ManagerID")
    private Integer managerID;

}