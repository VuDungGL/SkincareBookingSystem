package com.devices.app.repository;

import com.devices.app.dtos.AnnualStatisticsDto;
import com.devices.app.models.Users;
import jakarta.persistence.Tuple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<Users, String> {
    @Query("SELECT COUNT(U) FROM Users U WHERE U.roleID = 2")
    long getTotalMember();

    @Query(value = """
            WITH Months 
                AS (
                    SELECT 1 AS Month
                    UNION ALL
                    SELECT Month + 1 FROM Months WHERE Month < 12
                )
            SELECT M.Month,\s
                    COALESCE(Count(S.ID), 0) AS Total
            FROM Months AS M
            LEFT JOIN S_Users AS S ON M.Month = MONTH(S.CreateDate) AND YEAR(S.CreateDate) = :yearSearch
            GROUP BY M.Month
            ORDER BY M.Month
        """, nativeQuery = true)
    List<Tuple> AnnualNewMember(@Param("yearSearch") int yearSearch);
}
