package com.devices.app.dtos;

public class RatingFeedbackDto {
    private double rating;
    private int totalRating;

    public RatingFeedbackDto(double rating, int totalRating) {
        this.rating = rating;
        this.totalRating = totalRating;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getTotalRating() {
        return totalRating;
    }

    public void setTotalRating(int totalRating) {
        this.totalRating = totalRating;
    }
}
