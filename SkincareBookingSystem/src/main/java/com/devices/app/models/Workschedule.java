package com.devices.app.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Setter
@Getter
@Entity
@Table(name = "S_WorkSchedule")
public class Workschedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Column(name = "SpecialistID", nullable = false)
    private Integer specialistID;

    @Column(name = "WorkDate", nullable = false)
    private LocalDate workDate;

    @Column(name = "StartTime", nullable = false)
    private LocalTime startTime;

    @Column(name = "EndTime", nullable = false)
    private LocalTime endTime;

}