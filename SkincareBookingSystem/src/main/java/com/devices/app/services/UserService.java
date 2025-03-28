package com.devices.app.services;

import com.devices.app.dtos.AnnualStatisticsDto;
import com.devices.app.dtos.StaffInfoDto;
import com.devices.app.dtos.UserCreationRequest;
import com.devices.app.models.Users;
import jakarta.persistence.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.devices.app.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final FileService fileService;
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    public UserService(UserRepository userRepository, FileService fileService) {
        this.userRepository = userRepository;
        this.fileService = fileService;
    }


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


    public Page<StaffInfoDto> getListStaff(String search,int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        try {
            Page<Tuple> results = userRepository.getListStaff(search,pageable);

            if (results.isEmpty()) {
                return Page.empty(pageable);
            }

            List<StaffInfoDto> dtoList = results.stream().map(tuple -> new StaffInfoDto(
                    Optional.ofNullable(tuple.get("userID", Integer.class)).orElse(0), // Tránh null
                    Optional.ofNullable(tuple.get("userName", String.class)).orElse(""),
                    Optional.ofNullable(tuple.get("roleID", Integer.class)).orElse(0),
                    Optional.ofNullable(tuple.get("email", String.class)).orElse(""),
                    Optional.ofNullable(tuple.get("firstName", String.class)).orElse(""),
                    Optional.ofNullable(tuple.get("lastName", String.class)).orElse(""),
                    Optional.ofNullable(tuple.get("phone", String.class)).orElse(""),
                    Optional.ofNullable(tuple.get("userStatus", Integer.class)).orElse(0),
                    Optional.ofNullable(tuple.get("birthDay", String.class))
                            .map(str -> LocalDateTime.parse(str, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                                    .atOffset(ZoneOffset.UTC))
                            .orElse(null),
                    Optional.ofNullable(tuple.get("userCreateDate", String.class))
                            .map(str -> LocalDateTime.parse(str, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                                    .atOffset(ZoneOffset.UTC))
                            .orElse(null),
                    Optional.ofNullable(tuple.get("avt", String.class)).orElse(""),
                    Optional.ofNullable(tuple.get("gender", Integer.class)).orElse(0),

                    Optional.ofNullable(tuple.get("department", String.class)).orElse(""),
                    Optional.ofNullable(tuple.get("expertise", String.class)).orElse(""),
                    Optional.ofNullable(tuple.get("experience", Integer.class)).orElse(0),
                    Optional.ofNullable(tuple.get("salary", Integer.class)).orElse(0),
                    Optional.ofNullable(tuple.get("staffStatus", Integer.class)).orElse(0),
                    Optional.ofNullable(tuple.get("staffCreateDate", String.class))
                            .map(str -> LocalDateTime.parse(str, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                                    .atOffset(ZoneOffset.UTC))
                            .orElse(null),
                    Optional.ofNullable(tuple.get("bankAccount", String.class)).orElse(""),
                    Optional.ofNullable(tuple.get("bankName", String.class)).orElse(""),
                    Optional.ofNullable(tuple.get("position", String.class)).orElse("")
            )).collect(Collectors.toList());
            return new PageImpl<>(dtoList, pageable, results.getTotalElements());
        } catch (Exception ex) {
            return Page.empty(pageable);
        }
    }

    public void deleteUser(int id) {
        userRepository.deleteById(id);
    }

    @Transactional
    public String deleteStaff(int id) {
        try{
            Optional<Users> optionalUser = userRepository.findById(id);
            if (optionalUser.isPresent()) {
                Users user = optionalUser.get();
                String avatarPath = user.getAvt();

                if (avatarPath != null && !avatarPath.isEmpty()) {
                    fileService.deleteFile(avatarPath);
                }else{
                    System.out.println("File không tồn tại!");
                }

                userRepository.deleteById(id);
                userRepository.deleteStaffByStaffID(id);
                return "Xóa thành công";
            } else {
                return "Nhân viên không tồn tại";
            }
        }
        catch (Exception ex) {
            return "Xóa nhân viên thất bại!";
        }

    }
}
