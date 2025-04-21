package com.devices.app.controller.admin;

import com.devices.app.models.Feedback;
import com.devices.app.services.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("")
@CrossOrigin(origins = "*") // Cho phép gọi từ frontend (HTML/JS)
public class FeedbackController {
    private final FeedbackService feedbackService;

    @Autowired
    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    // ✅ GET all feedback (dùng để show trên giao diện)
    @GetMapping("/feedback/getAll")
    public List<Feedback> getAllFeedback() {
        return feedbackService.findAll();
    }

    // ✅ GET feedback theo ID
    @GetMapping("/{id}")
    public ResponseEntity<Feedback> getFeedbackById(@PathVariable Integer id) {
        Feedback feedback = feedbackService.findById(id);
        if (feedback == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(feedback, HttpStatus.OK);
    }

    // ✅ POST - Thêm feedback mới
    @PostMapping
    public ResponseEntity<Feedback> createFeedback(@RequestBody Feedback feedback) {
        Feedback created = feedbackService.save(feedback);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    // ✅ PUT - Cập nhật feedback theo ID
    @PutMapping("/{id}")
    public ResponseEntity<Feedback> updateFeedback(@PathVariable Integer id, @RequestBody Feedback updatedFeedback) {
        Feedback existing = feedbackService.findById(id);
        if (existing == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        existing.setUserID(updatedFeedback.getUserID());
        existing.setServiceID(updatedFeedback.getServiceID());
        existing.setRating(updatedFeedback.getRating());
        existing.setComment(updatedFeedback.getComment());
        existing.setCreateDate(updatedFeedback.getCreateDate());

        return new ResponseEntity<>(feedbackService.save(existing), HttpStatus.OK);
    }

    // ✅ DELETE - Xoá feedback theo ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFeedback(@PathVariable Integer id) {
        Feedback existing = feedbackService.findById(id);
        if (existing == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        feedbackService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
