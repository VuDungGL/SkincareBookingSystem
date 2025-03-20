package com.devices.app.services;

import com.devices.app.dtos.AnnualStatisticsDto;
import com.devices.app.dtos.BookingDto;
import com.devices.app.dtos.RevenueDto;
import com.devices.app.repository.BookingRepository;
import com.devices.app.repository.FeedbackRepository;
import jakarta.persistence.Tuple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

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
    public List<AnnualStatisticsDto> AnnualSale(int year){
        List<Tuple> results = bookingRepository.AnnualSale(year);

        return results.stream()
                .map(row -> new AnnualStatisticsDto(
                        ((Number) row.get(0)).intValue(),
                        ((Number) row.get(1)).doubleValue()
                ))
                .collect(Collectors.toList());
    }
    public List<AnnualStatisticsDto> AnnualSaleOnLast7Day(int isPaid, int year){
        List<Tuple> results = bookingRepository.AnnualSaleOnLast7Day(isPaid, year);

        return results.stream()
                .map(row -> new AnnualStatisticsDto(
                        ((Number) row.get(0)).intValue(),
                        ((Number) row.get(1)).doubleValue()
                ))
                .collect(Collectors.toList());
    }
}