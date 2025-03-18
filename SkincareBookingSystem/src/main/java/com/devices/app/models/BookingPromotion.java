package com.devices.app.models;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "S_BookingPromotion")
public class BookingPromotion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "BookingID", nullable = false)
    private Booking bookingID;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "PromotionID", nullable = false)
    private Promotion promotionID;

    @Column(name = "DiscountAmount", nullable = false, precision = 18, scale = 2)
    private BigDecimal discountAmount;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Booking getBookingID() {
        return bookingID;
    }

    public void setBookingID(Booking bookingID) {
        this.bookingID = bookingID;
    }

    public Promotion getPromotionID() {
        return promotionID;
    }

    public void setPromotionID(Promotion promotionID) {
        this.promotionID = promotionID;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

}