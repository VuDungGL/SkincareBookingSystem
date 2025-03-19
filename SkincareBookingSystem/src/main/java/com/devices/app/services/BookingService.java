package com.devices.app.services;

import com.devices.app.dtos.BookingDto;
import com.devices.app.dtos.RevenueDto;
import com.devices.app.repository.BookingRepository;
import com.devices.app.repository.FeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Service
public class BookingService {
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private FeedbackRepository feedbackRepository;

    public long GetTotal(int status) {
        return bookingRepository.getTotalBookingByStatus(status);
    }

    public Double GetRating() {
        Double rating = feedbackRepository.getRatingFeedback();
        return (rating != null) ? rating : 0.0;
    }

    public int GetTotalFeedback() {
        return feedbackRepository.getTotalFeedback();
    }

    public BookingDto BestSaler() {
        List<BookingDto> bestSellers = bookingRepository.findBestSaler();
        return bestSellers.isEmpty() ? null : bestSellers.get(0);
    }

    public RevenueDto getMonthlyRevenue() {
        LocalDate startDate = LocalDate.now().withDayOfMonth(1);
        LocalDate endDate = startDate.plusMonths(1);

        return bookingRepository.getMonthlyRevenue(Date.valueOf(startDate), Date.valueOf(endDate));
    }
}