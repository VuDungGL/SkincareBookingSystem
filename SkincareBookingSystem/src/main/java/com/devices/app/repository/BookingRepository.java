package com.devices.app.repository;

import com.devices.app.dtos.BookingDto;
import com.devices.app.dtos.RevenueDto;
import com.devices.app.models.Booking;
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

}
