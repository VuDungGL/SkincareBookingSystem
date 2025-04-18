package com.devices.app.repository;

import com.devices.app.models.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Integer> {
    @Query("SELECT AVG(F.rating) FROM Feedback F")
    Double getRatingFeedback();

    @Query("SELECT COUNT(F.id) FROM Feedback F")
    int getTotalFeedback();
    // Lấy tất cả feedback theo userID
    List<Feedback> findByUserID(Integer userID);

    // Lấy 5 feedback mới nhất
    List<Feedback> findTop5ByOrderByCreateDateDesc();

    // Lấy feedback theo service ID mới nhất
    List<Feedback> findTop3ByServiceIDOrderByCreateDateDesc(Integer serviceID);

    // Đếm feedback theo service
    @Query("SELECT COUNT(f.id) FROM Feedback f WHERE f.serviceID = ?1")
    int countByServiceID(Integer serviceID);

    // Trung bình rating theo service
    @Query("SELECT AVG(f.rating) FROM Feedback f WHERE f.serviceID = ?1")
    Double getAverageRatingByService(Integer serviceID);

    List<Feedback> findByServiceID(Integer serviceID);
}
