package com.devices.app.controller.admin;

import com.devices.app.dtos.dto.BookingDetailDto;
import com.devices.app.dtos.dto.FeedBackDto;
import com.devices.app.dtos.response.ApiResponse;
import com.devices.app.models.Feedback;
import com.devices.app.services.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
public class FeedbackController {
    private final FeedbackService feedbackService;

    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MASTER_ADMIN')")
    @GetMapping("/feedback/getListGoodFeedback")
    public ApiResponse<List<FeedBackDto>> getListGoodFeedback() {
        return feedbackService.getListGoodFeedback();
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MASTER_ADMIN')")
    @PostMapping("/feedback/getAllPageFeedback")
    public Page<FeedBackDto> getAll(@RequestBody Map<String, Object> request){
        int pageIndex = ((Number) request.getOrDefault("pageIndex", 0)).intValue();
        int pageSize = ((Number) request.getOrDefault("pageSize", 6)).intValue();
        String searchText = (String) request.getOrDefault("searchText", "");
        return feedbackService.getPageFeedback(searchText,pageIndex, pageSize);
    }
}
