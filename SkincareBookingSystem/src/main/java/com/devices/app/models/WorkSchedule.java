package com.devices.app.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Entity
@Table(name = "S_WorkSchedule")
public class WorkSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @NotNull
    @Column(name = "SpecialistID", nullable = false)
    private Integer specialistID;

    @NotNull
    @Column(name = "WorkDate", nullable = false)
    private LocalDate workDate;

    @NotNull
    @Column(name = "StartTime", nullable = false)
    private LocalTime startTime;

    @NotNull
    @Column(name = "EndTime", nullable = false)
    private LocalTime endTime;

}