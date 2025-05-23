package com.devices.app.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.time.OffsetDateTime;

@Getter
@Setter
@Entity

@Table(name = "S_Feedback")
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Column(name = "UserID")
    private Integer userID;

    @Column(name = "BookingDetailID")
    private Integer bookingDetailID;

    @Column(name = "Rating")
    private Integer rating;

    @Nationalized
    @Lob
    @Column(name = "Comment")
    private String comment;

    @Column(name = "CreateDate")
    private OffsetDateTime createDate;

}