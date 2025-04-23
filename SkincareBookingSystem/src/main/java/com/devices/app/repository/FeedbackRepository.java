package com.devices.app.repository;

import com.devices.app.dtos.dto.FeedBackDto;
import com.devices.app.models.Feedback;
import jakarta.persistence.Tuple;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Integer> {
    @Query("SELECT AVG(F.rating) FROM Feedback F")
    Double getRatingFeedback();

    @Query("SELECT COUNT(F.id) FROM Feedback F")
    int getTotalFeedback();

    @Query(value = """
        SELECT F.ID AS FeedbackID,
               F.Rating,
               F.Comment,
               FORMAT(F.CreateDate, 'yyyy-MM-dd HH:mm:ss') AS CreateDate,
               U.FirstName + ' ' + U.LastName AS FullName,
               U.Avt,
               U.Gender,
               S.ServiceName
        FROM S_Feedback AS F WITH (NOLOCK)
        INNER JOIN S_Users AS U WITH (NOLOCK) ON  F.UserID = U.ID
        INNER JOIN S_BookingDetail AS BD ON BD.ID = F.BookingDetailID
        INNER JOIN S_Services AS S WITH (NOLOCK) ON BD.ServiceID = S.ID
        WHERE F.Rating > 3
    """, nativeQuery = true)
    List<Tuple> getListGoodFeedback();

    @Query(value = """
        SELECT F.ID AS FeedbackID,
               F.Rating,
               F.Comment,
               FORMAT(F.CreateDate, 'yyyy-MM-dd HH:mm:ss') AS CreateDate,
               U.FirstName + ' ' + U.LastName AS FullName,
               U.Avt,
               U.Gender,
               S.ServiceName
        FROM S_Feedback AS F WITH (NOLOCK)
        INNER JOIN S_Users AS U WITH (NOLOCK) ON  F.UserID = U.ID
        INNER JOIN S_BookingDetail AS BD ON BD.ID = F.BookingDetailID
        INNER JOIN S_Services AS S WITH (NOLOCK) ON BD.ServiceID = S.ID
        WHERE 1 = 1 
            AND (
                :searchText IS NULL 
                OR U.FirstName LIKE %:searchText% 
                OR U.LastName LIKE %:searchText%
                OR S.ServiceName LIKE %:searchText%
            )
        """,
            countQuery = """
            SELECT COUNT(F.ID)
            FROM S_Feedback AS F WITH (NOLOCK)
            INNER JOIN S_Users AS U WITH (NOLOCK) ON  F.UserID = U.ID
            INNER JOIN S_BookingDetail AS BD ON BD.ID = F.BookingDetailID
            INNER JOIN S_Services AS S WITH (NOLOCK) ON BD.ServiceID = S.ID
            WHERE 1 = 1 
                AND (
                    :searchText IS NULL 
                    OR U.FirstName LIKE %:searchText% 
                    OR U.LastName LIKE %:searchText%
                    OR S.ServiceName LIKE %:searchText%
                )
            """, nativeQuery = true)
    Page<Tuple> getAllPageFeedback(@Param("searchText") String searchText, Pageable pageable);
}
