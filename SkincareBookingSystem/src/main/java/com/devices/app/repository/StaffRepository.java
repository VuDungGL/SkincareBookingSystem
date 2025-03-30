package com.devices.app.repository;

import com.devices.app.models.StaffInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StaffRepository extends JpaRepository<StaffInfo, Integer> {
}
