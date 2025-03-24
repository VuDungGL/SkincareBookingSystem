package com.devices.app.services;

import com.devices.app.dtos.AnnualStatisticsDto;
import com.devices.app.dtos.UserCreationRequest;
import com.devices.app.dtos.UserDto;
import com.devices.app.models.Users;
import jakarta.persistence.Tuple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.devices.app.repository.UserRepository;

import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public Users createRequest(UserCreationRequest request) {
        Users user = new Users();

        user.setRoleID(request.getRoleID());
        user.setUserName(request.getUserName());
        user.setPassword(request.getPassword());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhone(request.getPhone());
        user.setEmail(request.getEmail());

        return(userRepository.save(user));
    }

    public long GetTotalMember(){
        return userRepository.getTotalMember();
    }


    public List<AnnualStatisticsDto> AnnualNewMembers(int year){
        List<Tuple> results = userRepository.AnnualNewMember(year);

        return results.stream()
                .map(row -> new AnnualStatisticsDto(
                        ((Number) row.get(0)).intValue(),
                        ((Number) row.get(1)).doubleValue()
                ))
                .collect(Collectors.toList());
    }

}
