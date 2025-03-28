package com.devices.app.repository;

import com.devices.app.models.Department;
import jakarta.persistence.Tuple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, String> {
    @Query(value = """
            WITH totalMember AS (
                SELECT \s
                    S.Department AS DepartmentID,
                    COUNT(S.ID) AS TotalMember  -- Đếm số lượng nhân viên trong phòng ban
                FROM S_StaffInfo AS S WITH (NOLOCK)
                GROUP BY S.Department
            )
            SELECT \s
                TM.DepartmentID,
                TM.TotalMember,
                D.Department,
                D.Icon,
                D.ManagerID,
                U.Avt AS ManagerAvt,
                U.FirstName AS ManagerFirstName,
                U.LastName AS ManagerLastName,
                -- Lấy danh sách Avatar của nhân viên
                (SELECT STRING_AGG(SU.Avt, ',')
                 FROM S_StaffInfo AS SI WITH (NOLOCK)
                INNER JOIN S_Users AS SU WITH (NOLOCK) ON SU.ID = SI.StaffID
                 WHERE SI.Department = D.ID) AS MemberAvatars
            FROM totalMember AS TM
            INNER JOIN S_Department AS D WITH (NOLOCK) ON D.ID = TM.DepartmentID
            INNER JOIN S_Users AS U WITH (NOLOCK) ON U.ID = D.ManagerID;
            """, nativeQuery = true)
    List<Tuple> getDepartmentList();


}
