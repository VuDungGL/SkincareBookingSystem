package com.devices.app.services;

import com.devices.app.repository.BookingRepository;
import com.devices.app.repository.FeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Service
public class BookingService {
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private FeedbackRepository feedbackRepository;

    public long GetTotal() {
        return bookingRepository.getTotalBookingSuccess();
    }

    public Double GetRating() {
        Double rating = feedbackRepository.getRatingFeedback();
        return (rating != null) ? rating : 0.0;
    }

    public int GetTotalFeedback() {
        return feedbackRepository.getTotalFeedback();
    }
}
