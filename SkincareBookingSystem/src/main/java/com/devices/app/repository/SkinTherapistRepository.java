package com.devices.app.repository;

import com.devices.app.models.SkinTherapist;
import jakarta.persistence.Tuple;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface SkinTherapistRepository extends JpaRepository<SkinTherapist, Integer> {
    @Query(value= """
        SELECT
            S.ID AS SkinTherapistID,
            S.Email,
            S.FirstName,
            S.LastName,
            S.Phone,
            FORMAT(S.BirthDate, 'yyyy-MM-dd HH:mm:ss') AS BirthDate,
            S.Avt,
            S.Gender,
            S.Expertise,
            S.Experience,
            S.Salary,
            S.Status
            FROM S_SkinTherapist AS S
            WHERE 1 = 1
            AND (
                :searchText IS NULL 
                OR S.Email LIKE %:searchText% 
                OR S.Phone LIKE %:searchText%
                OR S.FirstName LIKE %:searchText%
                OR S.LastName LIKE %:searchText%
                OR S.Experience LIKE %:searchText%
            )
        """, countQuery = "SELECT COUNT(*) FROM S_SkinTherapist WHERE  1 = 1 AND (:searchText IS NULL OR Email LIKE %:searchText%)",
            nativeQuery = true)
    Page<Tuple> getListSkinTherapist(@Param("searchText") String searchText, Pageable pageable);

    @Query(value = """
            WITH BusyTherapists AS (
            SELECT DISTINCT bd.SkinTherapistID
            FROM S_WorkSchedule ws
            INNER JOIN S_BookingDetail bd ON ws.BookingDetailID = bd.ID
            WHERE ws.WorkDate = :workDate
              AND (
                      (CAST(:startTime AS time) BETWEEN ws.StartTime AND ws.EndTime) OR
                      (CAST(:endTime AS time) BETWEEN ws.StartTime AND ws.EndTime) OR
                      (ws.StartTime BETWEEN CAST(:startTime AS time) AND CAST(:endTime AS time)) OR
                      (ws.EndTime BETWEEN CAST(:startTime AS time) AND CAST(:endTime AS time))
                  )
            )
            SELECT
                S.ID,
                S.Email,
                S.FirstName,
                S.LastName,
                S.Phone,
                FORMAT(S.BirthDate, 'yyyy-MM-dd HH:mm:ss') AS BirthDate,
                S.CreateDate,
                S.Avt,
                S.Gender,
                S.Expertise,
                S.Experience,
                S.Salary,
                S.Status
            FROM S_SkinTherapist S
            LEFT JOIN BusyTherapists BT ON S.ID = BT.SkinTherapistID
            WHERE BT.SkinTherapistID IS NULL
    """,nativeQuery = true)
    List<SkinTherapist> getListSkinTherapistNotBusy(@Param("workDate") LocalDate workDate, @Param("startTime") LocalTime startTime, @Param("endTime") LocalTime endTime);
}
