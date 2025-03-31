package com.devices.app.services;

import com.devices.app.dtos.DepartmentDto;

import com.devices.app.repository.DepartmentRepository;
import jakarta.persistence.Tuple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class DepartmentService {
    @Autowired
    private DepartmentRepository departmentRepository;

    public List<DepartmentDto> getDepartmentList(){
        List<Tuple> results = departmentRepository.getDepartmentList();
        if(results.isEmpty()){
            return List.of();
        }
        return results.stream().map(tuple -> new DepartmentDto(
                Optional.ofNullable(tuple.get("departmentID", Integer.class)).orElse(0),
                Optional.ofNullable(tuple.get("department", String.class)).orElse(""),
                Optional.ofNullable(tuple.get("totalMember", Integer.class)).orElse(0),
                Optional.ofNullable(tuple.get("icon", String.class)).orElse(""),
                Optional.ofNullable(tuple.get("managerID", Integer.class)).orElse(0),
                Optional.ofNullable(tuple.get("managerAvt", String.class)).orElse(""),
                Optional.ofNullable(tuple.get("managerFirstName", String.class)).orElse(""),
                Optional.ofNullable(tuple.get("managerLastName", String.class)).orElse(""),
                Optional.ofNullable(tuple.get("memberAvatars", String.class))
                        .map(avatars -> Arrays.asList(avatars.split(","))) // Chuyển đổi chuỗi thành List<String>
                        .orElse(List.of())
        )).collect(Collectors.toList());
    }
}
