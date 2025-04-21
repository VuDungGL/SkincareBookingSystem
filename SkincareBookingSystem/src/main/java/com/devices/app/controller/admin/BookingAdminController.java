package com.devices.app.controller.admin;

import com.devices.app.dtos.dto.*;
import com.devices.app.services.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
public class BookingAdminController {
    private final  BookingService bookingService;
    private Map<String, Integer> request;

    public BookingAdminController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MASTER_ADMIN')")
    @PostMapping("/booking/GetTotal")
    long GetTotal(@RequestBody Map<String, Integer> request) {
        int status = request.get("status");
        return bookingService.GetTotal(status);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MASTER_ADMIN')")
    @GetMapping("/booking/GetRatingFeedback")
    public RatingFeedbackDto GetRatingFeedback() {
        return new RatingFeedbackDto(bookingService.GetRating(), bookingService.GetTotalFeedback());
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MASTER_ADMIN')")
    @GetMapping("/booking/BestSaler")
    public BookingDto BestSaler() {
        return bookingService.BestSaler();
    }


    @PreAuthorize("hasRole('ADMIN') or hasRole('MASTER_ADMIN')")
    @GetMapping("/booking/getRevenue")
    public RevenueDto getMonthlyRevenue() {
        return bookingService.getMonthlyRevenue();
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MASTER_ADMIN')")
    @PostMapping("/booking/annualSale")
    public List<AnnualStatisticsDto> annulSale(@RequestBody AnnualStatisticsDto request){
        return bookingService.AnnualSale(request.getYear());
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MASTER_ADMIN')")
    @PostMapping("/booking/revenueLast7day")
    public List<AnnualStatisticsDto> revenueLast7day(@RequestBody Map<String, Integer> request) {
        int isPaid = request.get("isPaid");
        int year = LocalDate.now().getYear();
        return bookingService.AnnualSaleOnLast7Day(isPaid,year);
    }


    @PreAuthorize("hasRole('ADMIN') or hasRole('MASTER_ADMIN')")
    @PostMapping("/booking/getHardWorking")
    public List<SkinTherapistDto> getHardWorking(@RequestBody Map<String, Integer> request) {
        int pageIndex = request.getOrDefault("pageIndex", 1);
        int pageSize = request.getOrDefault("pageSize", 4);

        if (pageIndex < 1) pageIndex = 1;
        if (pageSize <= 0) pageSize = 4;
        return bookingService.getUsersByPage(pageIndex, pageSize);
    }
}
