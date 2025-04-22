package com.devices.app.repository;

import com.devices.app.models.BookingDetail;
import jakarta.persistence.Tuple;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingDetailRepository extends JpaRepository<BookingDetail, Integer> {
    @Query(value = """
                    SELECT
                        BD.ID AS BookingDetailID,
                        BD.Email,
                        BD.FullName,
                        BD.Phone,
                        FORMAT(B.BookingDate, 'yyyy-MM-dd HH:mm:ss') AS CreateDate,
                        BD.SkinTherapistID,
                        BD.ServiceID,
                        BD.Price,
                        BD.IsPaid,
                        BD.Status,
                        W.WorkDate,
                        W.StartTime,
                        W.EndTime
                        FROM S_BookingDetail AS BD WITH (NOLOCK)
                        INNER JOIN S_Booking AS B ON BD.BookingID = B.ID
                        INNER JOIN S_WorkSchedule AS W ON BD.ID = W.BookingDetailID
                        WHERE BD.IsPaid = :isPaid
                        AND (
                            :searchText IS NULL\s
                            OR BD.Email LIKE %:searchText%\s
                            OR BD.Phone LIKE %:searchText%
                            OR BD.FullName LIKE %:searchText%
                            OR BD.Status LIKE %:searchText%
                            OR BD.ServiceID LIKE %:searchText%
                        )
                     ORDER BY
                            CASE WHEN BD.SkinTherapistID IS NULL THEN 0 ELSE 1 END,
                            BD.Status ASC
                    """, countQuery = "SELECT COUNT(*) FROM S_BookingDetail AS BD WHERE  1 = 1 AND (:searchText IS NULL OR BD.Email LIKE %:searchText%)"
            ,nativeQuery = true)
    Page<Tuple> bookingDetail(@Param("searchText") String searchText, Pageable pageable, @Param("isPaid") boolean isPaid);

    @Query(value = """
        SELECT B.UserID
        FROM S_Booking AS B WITH (NOLOCK)
        INNER JOIN S_BookingDetail AS BD WITH (NOLOCK) ON BD.BookingID = B.ID
        WHERE BD.ID = :bookingDetailID
    """,nativeQuery = true)
    Integer getUserIDFromBooking(@Param("bookingDetailID") Integer bookingDetailID);

    @Query(value = """
        SELECT SUM(BD.ID)
        FROM S_Booking AS B WITH (NOLOCK)
        INNER JOIN S_BookingDetail AS BD WITH (NOLOCK) ON BD.BookingID = B.ID
        WHERE B.UserID = :userID AND BD.IsPaid = 1
    """,nativeQuery = true)
    Integer getTotalBookingSuccessful(@Param("userID") Integer userID);
}
