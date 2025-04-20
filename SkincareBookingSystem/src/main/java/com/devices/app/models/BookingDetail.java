package com.devices.app.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

@Getter
@Setter
@Entity
@Table(name = "S_BookingDetail")
public class BookingDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Column(name = "BookingID")
    private Integer bookingID;

    @Column(name = "ServiceID")
    private Integer serviceID;

    @Column(name = "SkinTherapistID")
    private Integer skinTherapistID;

    @Column(name = "Price")
    private Double price;

    @Column(name = "Status")
    private Integer status;

    @Column(name = "IsPaid")
    private Boolean isPaid;

    @Column(name = "PromotionID")
    private Integer promotionID;

    @Nationalized
    @Lob
    @Column(name = "FullName")
    private String fullName;

    @Size(max = 12)
    @Nationalized
    @Column(name = "Phone", length = 12)
    private String phone;

    @Size(max = 50)
    @Nationalized
    @Column(name = "Email", length = 50)
    private String email;

    @Nationalized
    @Lob
    @Column(name = "Note")
    private String note;

}