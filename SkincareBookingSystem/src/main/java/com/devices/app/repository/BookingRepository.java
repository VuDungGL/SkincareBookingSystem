package com.devices.app.repository;

import com.devices.app.dtos.AnnualStatisticsDto;
import com.devices.app.dtos.BookingDto;
import com.devices.app.dtos.RevenueDto;
import com.devices.app.models.Booking;
import jakarta.persistence.Tuple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, String> {
    @Query("SELECT COUNT(B) FROM BookingDetail B WHERE B.status = :status")
    long getTotalBookingByStatus(@Param("status") int status);
//    Status = 0 : Vừa tạo
//           = 1 : Thành công
//           = 2 : đã hủy
//           = 3 : Hoãn
    @Query("SELECT new com.devices.app.dtos.BookingDto(BD.serviceID, S.serviceName, COUNT(BD.serviceID)) " +
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
        WHERE B.BookingDate >= :startDate AND B.BookingDate < :endDate
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
                SELECT DAY(DATEADD(DAY, -n, GETDATE())) AS [Day]
                FROM (VALUES (0), (1), (2), (3), (4), (5), (6)) AS Days(n)
            )
            SELECT\s
                L.[Day],
                COALESCE(SUM(BD.Price), 0) AS Total
            FROM Last7Days AS L
            LEFT JOIN S_Booking AS B WITH (NOLOCK) ON DAY(B.BookingDate) = L.[Day]
            LEFT JOIN S_BookingDetail AS BD WITH (NOLOCK) ON BD.BookingID = B.ID AND BD.IsPaid = :isPaid AND YEAR(B.BookingDate) = :yearSearch
            GROUP BY L.[Day]
            ORDER BY L.[Day]
    """,nativeQuery = true)
    List<Tuple> AnnualSaleOnLast7Day(@Param("isPaid") int isPaid,@Param("yearSearch")int yearSearch);
}
