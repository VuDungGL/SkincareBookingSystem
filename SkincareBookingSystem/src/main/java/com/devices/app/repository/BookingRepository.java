package com.devices.app.repository;

import com.devices.app.models.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends JpaRepository<Booking, String> {
    @Query("SELECT COUNT(B) FROM BookingDetail B WHERE B.status = 1")
    long getTotalBookingSuccess();
//    Status = 0 : Vừa tạo
//           = 1 : Thành công
//           = 2 : đã hủy
//           = 3 : Hoãn


}
