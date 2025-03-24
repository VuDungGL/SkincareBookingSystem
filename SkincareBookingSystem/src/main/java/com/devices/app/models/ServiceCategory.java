package com.devices.app.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

@Setter
@Getter
@Entity
@Table(name = "S_ServiceCategory")
public class ServiceCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Nationalized
    @Column(name = "CategoryName", nullable = false, length = 50)
    private String categoryName;

    @Nationalized
    @Lob
    @Column(name = "Description")
    private String description;

}