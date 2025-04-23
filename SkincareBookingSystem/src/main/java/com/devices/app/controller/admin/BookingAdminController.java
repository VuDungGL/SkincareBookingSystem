package com.devices.app.controller.admin;

import com.devices.app.dtos.dto.*;
import com.devices.app.dtos.response.ApiResponse;
import com.devices.app.models.BookingDetail;
import com.devices.app.models.SkinTherapist;
import com.devices.app.services.BookingService;
import com.devices.app.services.ServicesService;
import com.devices.app.services.SkinTherapistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/admin")
public class BookingAdminController {
    private final  BookingService bookingService;
    private final ServicesService servicesService;
    private final SkinTherapistService skinTherapistService;
    private Map<String, Integer> request;


    public BookingAdminController(BookingService bookingService, ServicesService servicesService, SkinTherapistService skinTherapistService) {
        this.bookingService = bookingService;
        this.servicesService = servicesService;
        this.skinTherapistService = skinTherapistService;
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

    @PreAuthorize("hasRole('ADMIN') or hasRole('MASTER_ADMIN')")
    @PostMapping("/booking/getAll")
    public Page<BookingDetailDto> getAll(@RequestBody Map<String, Object> request){
        int pageIndex = ((Number) request.getOrDefault("pageIndex", 0)).intValue();
        int pageSize = ((Number) request.getOrDefault("pageSize", 6)).intValue();
        String searchText = (String) request.getOrDefault("searchText", "");
        boolean isPaid = (boolean) request.getOrDefault("isPaid", false);
        return bookingService.getListBooking(searchText,pageIndex, pageSize, isPaid);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MASTER_ADMIN')")
    @PostMapping("/booking/getListTherapistFree")
    public List<SkinTherapist> listTherapistFree(@RequestBody Map<String, Object> request) {
        String workDateStr = (String) request.get("workDate");
        String startTimeStr = (String) request.get("startTime");

        LocalDate workDate = LocalDate.parse(workDateStr);
        LocalTime startTime = LocalTime.parse(startTimeStr);
        int serviceID = ((Number) request.getOrDefault("serviceID", 0)).intValue();
        int duration = servicesService.findById(serviceID).getData().getDuration();
        return skinTherapistService.getListSkinTherapistNotBusy(workDate,startTime,startTime.plusMinutes(duration));
    }
    @PreAuthorize("hasRole('ADMIN') or hasRole('MASTER_ADMIN')")
    @PostMapping("/booking/checkOutBooking")
    public ApiResponse<String> checkOutBooking(@RequestBody Map<String, Integer> request) {
        Integer bookingDetailID = request.get("bookingDetailID");
        return  bookingService.checkOutBooking(bookingDetailID);
    }
}
