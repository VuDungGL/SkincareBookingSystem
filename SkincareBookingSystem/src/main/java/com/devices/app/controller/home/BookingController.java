package com.devices.app.controller.home;

import com.devices.app.dtos.dto.HistoriesDto;
import com.devices.app.dtos.requests.CreateBookingRequest;
import com.devices.app.dtos.response.ApiResponse;
import com.devices.app.models.BookingDetail;
import com.devices.app.services.BookingService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class BookingController {
    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping("/booking/createBooking")
    public ResponseEntity<?> createBooking(@RequestBody CreateBookingRequest request) {
        ApiResponse<BookingDetail> response = bookingService.onBooking(request);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/booking/onChooseTherapist")
    public ResponseEntity<?> onChooseTherapist(@RequestBody Map<String, Integer> request) {
        Integer therapistID = request.get("therapistID");
        Integer bookingDetailID = request.get("bookingDetailID");
        ApiResponse<String> response = bookingService.onChooseTherapist(therapistID, bookingDetailID);
        return ResponseEntity.ok(response);
    }
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/booking/getHistoryBooking")
    public Page<HistoriesDto> getHistoryBooking(@RequestBody Map<String, Object> request){
        int pageIndex = ((Number) request.getOrDefault("pageIndex", 0)).intValue();
        int pageSize = ((Number) request.getOrDefault("pageSize", 6)).intValue();
        String searchText = (String) request.getOrDefault("searchText", "");
        int userID = Integer.parseInt((String) request.getOrDefault("userID", "0"));
        return bookingService.getListHistoryBooking(searchText,pageIndex, pageSize, userID);
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/booking/onCancelBooking")
    public ResponseEntity<?> onCancelBooking(@RequestBody Map<String, Integer> request) {
        Integer bookingDetailID = request.get("bookingDetailID");
        ApiResponse<String> response = bookingService.onCancelBooking(bookingDetailID);
        return ResponseEntity.ok(response);
    }
}
