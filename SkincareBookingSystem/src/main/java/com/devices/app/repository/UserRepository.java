package com.devices.app.repository;

import com.devices.app.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<Users, String> {
    @Query("SELECT COUNT(U) FROM Users U WHERE U.roleID = 2")
    long getTotalMember();
}
