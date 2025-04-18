package com.devices.app.services;
import com.devices.app.models.Feedback;
import com.devices.app.repository.FeedbackRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
@Service
public class FeedbackService {
    private final FeedbackRepository feedbackRepository;

    public FeedbackService(FeedbackRepository feedbackRepository) {
        this.feedbackRepository = feedbackRepository;
    }

    // Lấy tất cả feedback
    public List<Feedback> findAll() {
        return feedbackRepository.findAll();
    }

    // Lấy feedback theo ID
    public Feedback findById(Integer id) {
        Optional<Feedback> feedback = feedbackRepository.findById(id);
        return feedback.orElse(null);  // Nếu không tìm thấy, trả về null
    }

    // Thêm hoặc cập nhật feedback
    public Feedback save(Feedback feedback) {
        return feedbackRepository.save(feedback);
    }

    // Xoá feedback theo ID
    public void deleteById(Integer id) {
        feedbackRepository.deleteById(id);
    }
    public List<Feedback> findByServiceID(Integer serviceID) {
        return feedbackRepository.findByServiceID(serviceID);
    }

    // Lấy feedback theo user ID
    public List<Feedback> findByUserID(Integer userID) {
        return feedbackRepository.findByUserID(userID);
    }

    // Lấy 5 feedback mới nhất
    public List<Feedback> findLatestFeedback() {
        return feedbackRepository.findTop5ByOrderByCreateDateDesc();
    }

    // Lấy 3 feedback mới nhất theo service
    public List<Feedback> findTop3ByService(Integer serviceID) {
        return feedbackRepository.findTop3ByServiceIDOrderByCreateDateDesc(serviceID);
    }

    // Trung bình rating toàn bộ
    public Double getAverageRating() {
        return feedbackRepository.getRatingFeedback();
    }

    // Tổng số feedback
    public int getTotalFeedback() {
        return feedbackRepository.getTotalFeedback();
    }

    // Trung bình rating theo service
    public Double getAverageRatingByService(Integer serviceID) {
        return feedbackRepository.getAverageRatingByService(serviceID);
    }

    // Tổng feedback theo service
    public int countByServiceID(Integer serviceID) {
        return feedbackRepository.countByServiceID(serviceID);
    }
}
