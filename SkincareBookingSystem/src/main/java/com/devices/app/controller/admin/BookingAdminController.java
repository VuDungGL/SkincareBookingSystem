package com.devices.app.controller.admin;

import com.devices.app.dtos.BookingDto;
import com.devices.app.dtos.RatingFeedbackDto;
import com.devices.app.services.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BookingAdminController {
    @Autowired
    private BookingService bookingService;

    @GetMapping("/booking/GetTotal")
    long GetTotal() {
        return bookingService.GetTotal();
    }

    @GetMapping("/booking/GetRatingFeedback")
    public RatingFeedbackDto GetRatingFeedback() {
        return new RatingFeedbackDto(bookingService.GetRating(), bookingService.GetTotalFeedback());
    }
}
