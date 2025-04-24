package com.devices.app.controller.home;

import com.devices.app.dtos.dto.FeedBackDto;
import com.devices.app.dtos.response.ApiResponse;
import com.devices.app.services.FeedbackService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class TestimonialController {
    private final FeedbackService feedbackService;

    public TestimonialController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @GetMapping("/testimonial")
    public String Testimonial(Model model) {
        ApiResponse<List<FeedBackDto>> feedback = feedbackService.getListGoodFeedback();
        model.addAttribute("feedbacks", feedback.getData());
        return "home/testimonial";
    }
}
