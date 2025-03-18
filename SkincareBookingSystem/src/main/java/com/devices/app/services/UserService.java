package com.devices.app.services;

import com.devices.app.dtos.UserCreationRequest;
import com.devices.app.models.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.devices.app.repository.UserRepository;

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
}
