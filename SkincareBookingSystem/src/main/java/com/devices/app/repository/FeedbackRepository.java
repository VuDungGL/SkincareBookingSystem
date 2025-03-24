package com.devices.app.repository;

import com.devices.app.models.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, String> {
    @Query("SELECT AVG(F.rating) FROM Feedback F")
    Double getRatingFeedback();

    @Query("SELECT COUNT(F.id) FROM Feedback F")
    int getTotalFeedback();
}
