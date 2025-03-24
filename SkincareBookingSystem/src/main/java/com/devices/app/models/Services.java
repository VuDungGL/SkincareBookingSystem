package com.devices.app.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

@Setter
@Getter
@Entity
@Table(name = "S_Services")
public class Services {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Column(name = "CategoryID", nullable = false)
    private Integer categoryID;

    @Nationalized
    @Column(name = "ServiceName", nullable = false, length = 100)
    private String serviceName;

    @Nationalized
    @Lob
    @Column(name = "Description")
    private String description;

    @Column(name = "Price", nullable = false)
    private Double price;

    @Column(name = "Duration", nullable = false)
    private Integer duration;

}