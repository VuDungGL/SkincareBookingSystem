package com.devices.app.dtos.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RatingFeedbackDto {
    private double rating;
    private int totalRating;

    public RatingFeedbackDto(double rating, int totalRating) {
        this.rating = rating;
        this.totalRating = totalRating;
    }

}
