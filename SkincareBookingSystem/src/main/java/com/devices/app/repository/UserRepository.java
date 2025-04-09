package com.devices.app.repository;

import com.devices.app.dtos.dto.UserDto;
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
import java.util.Optional;

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


    boolean existsByUserName(String userName);
    boolean existsByEmail(String email);

    List<Users> findAllByStatus(int status);

    @Query(value = """
        SELECT  U.ID AS UserID,
                U.UserName,
                U.RoleID,
                U.Email,
                U.Phone,
                U.FirstName,
                U.LastName,
                U.Avt,
                U.Gender,
                FORMAT(U.BirthDay, 'yyyy-MM-dd HH:mm:ss') AS BirthDate,
                U.Status
        FROM S_Users AS U WITH (NOLOCK)
        WHERE U.RoleID = :roleID
        AND(
            :searchText IS NULL 
                OR U.UserName LIKE %:searchText%
                OR U.Email LIKE %:searchText% 
                OR U.Phone LIKE %:searchText%
                OR U.FirstName LIKE %:searchText%
                OR U.LastName LIKE %:searchText%
            )
        ORDER BY U.Status DESC
    """,countQuery = "SELECT COUNT(*) FROM S_Users AS U WHERE  U.RoleID = :roleID AND (:searchText IS NULL OR UserName LIKE %:searchText%)"
    ,nativeQuery = true)
    Page<Tuple> findAllUserByUserRole(@Param("searchText") String searchText,@Param("roleID") int roleID, Pageable pageable);

    List<Users> findAllByRoleID(int roleID);

    @Query(value = """
        SELECT  U.ID,
                U.UserName,
                U.RoleID,
                U.Email,
                U.Phone,
                U.FirstName,
                U.LastName,
                U.Avt,
                U.Gender,
                FORMAT(U.BirthDay, 'yyyy-MM-dd HH:mm:ss') AS BirthDay,
                FORMAT(U.CreateDate, 'yyyy-MM-dd HH:mm:ss') AS CreateDate,
                U.Status,
                U.Password
        FROM S_Users AS U WITH (NOLOCK) 
        WHERE U.UserName = :userName
    """,nativeQuery = true)
    Users findByUserName(@Param("userName") String userName);
}
