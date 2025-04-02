package com.devices.app.repository;

import com.devices.app.models.Users;
import jakarta.persistence.Tuple;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<Users, Integer> {
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



    boolean existsByUserName(String userName);
    boolean existsByEmail(String email);
}
