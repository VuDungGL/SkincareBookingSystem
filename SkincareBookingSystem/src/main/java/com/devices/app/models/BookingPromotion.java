package com.devices.app.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "S_BookingPromotions")
public class BookingPromotion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Column(name = "BookingID")
    private Integer bookingID;

    @Column(name = "PromotionID")
    private Integer promotionID;

    @Column(name = "DiscountAmount")
    private Double discountAmount;

}