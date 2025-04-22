package com.devices.app.repository;

import com.devices.app.dtos.dto.BookingDto;
import com.devices.app.dtos.dto.RevenueDto;
import com.devices.app.dtos.dto.SkinTherapistDto;
import com.devices.app.models.Booking;
import jakarta.persistence.Tuple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, String> {
    @Query("SELECT COUNT(B) FROM BookingDetail B WHERE B.status = :status")
    long getTotalBookingByStatus(@Param("status") int status);
//    Status = 0 : Vừa tạo
//           = 1 : Thành công
//           = 2 : đã hủy
//           = 3 : Hoãn
    @Query("SELECT new com.devices.app.dtos.dto.BookingDto(BD.serviceID, S.serviceName, COUNT(BD.serviceID)) " +
            "FROM BookingDetail BD " +
            "JOIN Services S ON BD.serviceID = S.id " +
            "GROUP BY BD.serviceID, S.serviceName " +
            "ORDER BY COUNT(BD.serviceID) DESC")
    List<BookingDto> findBestSaler();

    @Query(value = """
        SELECT 
            YEAR(B.BookingDate) AS Year,
            FORMAT(B.BookingDate, 'MMMM', 'en-US') AS MonthName,
            SUM(BD.Price) AS TotalRevenue
        FROM S_BookingDetail BD
        INNER JOIN S_Booking B ON BD.BookingID = B.ID
        WHERE B.BookingDate >= :startDate AND B.BookingDate < :endDate AND BD.Status <> 2
        GROUP BY YEAR(B.BookingDate), FORMAT(B.BookingDate, 'MMMM', 'en-US')
    """, nativeQuery = true)
    RevenueDto getMonthlyRevenue(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query(value = """
        WITH Months 
            AS (
                 SELECT 1 AS Month
                 UNION ALL
                 SELECT Month + 1 FROM Months WHERE Month < 12
             )
         SELECT M.Month,\s
                COALESCE(SUM(BD.Price), 0) AS Total
         FROM Months AS M
         LEFT JOIN S_Booking AS B ON M.Month = MONTH(B.BookingDate) AND YEAR(B.BookingDate) = :yearSearch
         LEFT JOIN S_BookingDetail AS BD ON B.ID = BD.BookingID AND BD.IsPaid = 1
         GROUP BY M.Month
         ORDER BY M.Month
        """, nativeQuery = true)
    List<Tuple> AnnualSale(@Param("yearSearch") int yearSearch);

    @Query(value = """
            WITH Last7Days AS (
                SELECT CAST(DATEADD(DAY, -n, GETDATE()) AS DATE) AS [Date]
                FROM (VALUES (0), (1), (2), (3), (4), (5), (6)) AS Days(n)
            )
            SELECT
                DAY(L.[Date]) AS [Day],
                COALESCE(SUM(BD.Price), 0) AS Total
            FROM Last7Days AS L
            LEFT JOIN S_Booking AS B WITH (NOLOCK)
                ON CAST(B.BookingDate AS DATE) = L.[Date]
            LEFT JOIN S_BookingDetail AS BD WITH (NOLOCK)
                ON BD.BookingID = B.ID AND BD.IsPaid = :isPaid AND YEAR(B.BookingDate) = :yearSearch AND BD.Status <> 2
            GROUP BY L.[Date]
            ORDER BY L.[Date]
    """,nativeQuery = true)
    List<Tuple> AnnualSaleOnLast7Day(@Param("isPaid") int isPaid,@Param("yearSearch")int yearSearch);

    @Query(value= """
           WITH Staffs AS (
                   SELECT
                       S.ID AS SkinTerapistID,
                       S.Email,
                       S.FirstName,
                       S.LastName,
                       S.Phone,
                       S.Avt,
                       COALESCE(SUM(BD.Price), 0) AS Total,
                       COALESCE(COUNT(B.ID), 0) AS TotalTask,
                       COALESCE(COUNT(B.ID) * 100.0 / NULLIF(SUM(COUNT(B.ID)) OVER (), 1), 0) AS PercentTask
                   FROM S_SkinTherapist AS S WITH (NOLOCK)
                   LEFT JOIN S_BookingDetail AS BD WITH (NOLOCK) ON BD.SkinTherapistID = S.ID
                   LEFT JOIN S_Booking AS B WITH (NOLOCK) ON B.ID = BD.BookingID AND MONTH(B.BookingDate) = MONTH(GETDATE())
                   GROUP BY S.ID, S.Email, S.FirstName, S.LastName, S.Phone, S.Avt
               )
               SELECT *
               FROM Staffs
               ORDER BY Total DESC
               OFFSET :offSet ROWS FETCH NEXT :pageSize ROWS ONLY;
        """, nativeQuery = true)
    List<SkinTherapistDto> GetListUserWorkMonth(@Param("offSet") int offSet, @Param("pageSize") int pageSize);

}
