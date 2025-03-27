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
@Table(name = "S_Services")
public class Services {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @NotNull
    @Column(name = "CategoryID", nullable = false)
    private Integer categoryID;

    @Size(max = 100)
    @NotNull
    @Nationalized
    @Column(name = "ServiceName", nullable = false, length = 100)
    private String serviceName;

    @Nationalized
    @Lob
    @Column(name = "Description")
    private String description;

    @NotNull
    @Column(name = "Price", nullable = false)
    private Double price;

    @NotNull
    @Column(name = "Duration", nullable = false)
    private Integer duration;

    @Nationalized
    @Lob
    @Column(name = "Illustration")
    private String illustration;

}