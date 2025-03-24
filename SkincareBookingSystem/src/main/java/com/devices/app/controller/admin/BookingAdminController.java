package com.devices.app.controller.admin;

import com.devices.app.dtos.*;
import com.devices.app.services.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
public class BookingAdminController {
    @Autowired
    private BookingService bookingService;
    private Map<String, Integer> request;

    @PostMapping("/booking/GetTotal")
    long GetTotal(@RequestBody Map<String, Integer> request) {
        int status = request.get("status");
        return bookingService.GetTotal(status);
    }

    @GetMapping("/booking/GetRatingFeedback")
    public RatingFeedbackDto GetRatingFeedback() {
        return new RatingFeedbackDto(bookingService.GetRating(), bookingService.GetTotalFeedback());
    }

    @GetMapping("/booking/BestSaler")
    public BookingDto BestSaler() {
        return bookingService.BestSaler();
    }

    @GetMapping("/booking/getRevenue")
    public RevenueDto getMonthlyRevenue() {
        return bookingService.getMonthlyRevenue();
    }

    @PostMapping("/booking/annualSale")
    public List<AnnualStatisticsDto> annulSale(@RequestBody AnnualStatisticsDto request){
        return bookingService.AnnualSale(request.getYear());
    }
    @PostMapping("/booking/revenueLast7day")
    public List<AnnualStatisticsDto> revenueLast7day(@RequestBody Map<String, Integer> request) {
        int isPaid = request.get("isPaid");
        int year = LocalDate.now().getYear();
        return bookingService.AnnualSaleOnLast7Day(isPaid,year);
    }
    @PostMapping("/booking/getHardWorking")
    public List<UserDto> getHardWorking(@RequestBody Map<String, Integer> request) {
        int pageIndex = request.getOrDefault("pageIndex", 1);
        int pageSize = request.getOrDefault("pageSize", 4);

        if (pageIndex < 1) pageIndex = 1;
        if (pageSize <= 0) pageSize = 4;
        return bookingService.getUsersByPage(pageIndex, pageSize);
    }
}
