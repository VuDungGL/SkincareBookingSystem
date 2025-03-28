package com.devices.app.repository;

import com.devices.app.models.Users;
import jakarta.persistence.Tuple;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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


    @Query(value= """
        SELECT
            U.ID AS userID,
            U.UserName,
            U.RoleID,
            U.Email,
            U.Password,
            U.FirstName,
            U.LastName,
            U.Phone,
            U.Status AS userStatus,
            FORMAT(U.BirthDay, 'yyyy-MM-dd HH:mm:ss') AS BirthDay,
            FORMAT(U.CreateDate, 'yyyy-MM-dd HH:mm:ss') AS UserCreateDate,
            U.Avt,
            U.Gender,
            D.Department,
            S.Expertise,
            S.Experience,
            S.Salary,
            S.Status AS staffStatus,
            FORMAT(S.CreateDate, 'yyyy-MM-dd HH:mm:ss') AS StaffCreateDate,
            S.BankAccount,
            S.BankName,
            S.Position
            FROM S_Users AS U
            LEFT JOIN S_StaffInfo AS S ON S.StaffID = U.ID
            LEFT JOIN S_Department AS D ON D.ID = S.Department
            WHERE U.RoleID = 3
            AND (
                :searchText IS NULL 
                OR U.UserName LIKE %:searchText% 
                OR U.Email LIKE %:searchText% 
                OR U.Phone LIKE %:searchText%
                OR U.FirstName LIKE %:searchText%
                OR U.LastName LIKE %:searchText%
                OR D.Department LIKE %:searchText%
                OR S.Position LIKE %:searchText%
            )
            ORDER BY D.ID
        """, countQuery = "SELECT COUNT(*) FROM S_Users WHERE RoleID = 3  AND (:searchText IS NULL OR UserName LIKE %:searchText%)",
            nativeQuery = true)
    Page<Tuple> getListStaff(@Param("searchText") String searchText, Pageable pageable);
    
}
