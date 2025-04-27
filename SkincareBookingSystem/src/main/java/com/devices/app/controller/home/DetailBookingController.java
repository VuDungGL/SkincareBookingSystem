package com.devices.app.controller.home;

import com.devices.app.dtos.response.ApiResponse;
import com.devices.app.models.BookingDetail;
import com.devices.app.services.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class DetailBookingController {
    private final BookingService bookingService;
    private final UserService userService;
    private final ServicesService servicesService;
    private final SkinTherapistService skinTherapistService;
    private final FeedbackService feedbackService;

    public DetailBookingController(BookingService bookingService, UserService userService, ServicesService servicesService, SkinTherapistService skinTherapistService, FeedbackService feedbackService) {
        this.bookingService = bookingService;
        this.userService = userService;
        this.servicesService = servicesService;
        this.skinTherapistService = skinTherapistService;
        this.feedbackService = feedbackService;
    }

    @GetMapping("DetailBooking")
    public String detailBooking(@RequestParam("bookingDetailID") Integer bookingDetailID, Model model) {
        ApiResponse<BookingDetail> booking =  bookingService.getBookingDetail(bookingDetailID);
        model.addAttribute("bookingDetail", booking.getData());
        model.addAttribute("booking", bookingService.getBooking(bookingDetailID).getData());
        model.addAttribute("serviceInfo", servicesService.findById(booking.getData().getServiceID()).getData());

        if (feedbackService.getFeedbackByBookingDetailID(bookingDetailID).getData() != null) {
            model.addAttribute("feedback", feedbackService.getFeedbackByBookingDetailID(bookingDetailID).getData());
            model.addAttribute("hasFeedback", true);
        } else {
            model.addAttribute("feedback", null);
            model.addAttribute("hasFeedback", false);
        }
        if (booking.getData().getSkinTherapistID() != null) {
            model.addAttribute("therapist", skinTherapistService.getSkinTherapistById(booking.getData().getSkinTherapistID()));
        } else {
            model.addAttribute("therapist", null);
        }
        return "home/detailBooking";
    }
}
