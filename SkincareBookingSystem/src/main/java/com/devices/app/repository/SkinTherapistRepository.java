package com.devices.app.repository;

import com.devices.app.models.SkinTherapist;
import jakarta.persistence.Tuple;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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
}
