package com.devices.app.services;

import com.devices.app.dtos.AnnualStatisticsDto;
import com.devices.app.dtos.StaffInfoDto;
import com.devices.app.dtos.UserCreationRequest;
import com.devices.app.models.StaffInfo;
import com.devices.app.models.Users;
import com.devices.app.repository.StaffRepository;
import jakarta.persistence.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.devices.app.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
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
    private final PasswordEncoder passwordEncoder;
    private final StaffRepository staffRepository;

    @Autowired
    public UserService(UserRepository userRepository, FileService fileService, PasswordEncoder passwordEncoder, StaffRepository staffRepository) {
        this.userRepository = userRepository;
        this.fileService = fileService;
        this.passwordEncoder = passwordEncoder;
        this.staffRepository = staffRepository;
    }


    @Transactional
    public Users createRequest(UserCreationRequest request) {
        // Kiểm tra trùng lặp userName và email
        if (userRepository.existsByUserName(request.getUserName())) {
            throw new IllegalArgumentException("Tên đăng nhập đã tồn tại!");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email đã tồn tại!");
        }

        // Tạo mới Users entity
        Users user = new Users();
        user.setUserName(request.getUserName());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRoleID(request.getRoleID());
        user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhone(request.getPhone());
        user.setStatus(request.getUserStatus());
        user.setCreateDate(Instant.now());
        user.setAvt(request.getAvt());
        user.setGender(request.getGender());

        // Lưu user vào database
        Users savedUser = userRepository.save(user);

        // Tạo mới StaffInfo entity và liên kết với user
        StaffInfo staffInfo = new StaffInfo();
        staffInfo.setStaffID(savedUser.getId()); // Gán ID user vào StaffInfo
        staffInfo.setDepartment(request.getDepartment());
        staffInfo.setExpertise(request.getExpertise());
        staffInfo.setExperience(request.getExperience());
        staffInfo.setSalary(request.getSalary());
        staffInfo.setStatus(request.getStaffStatus());
        staffInfo.setCreateDate(LocalDate.now());
        staffInfo.setBankAccount(request.getBankAccount());
        staffInfo.setBankName(request.getBankName());
        staffInfo.setPosition(request.getPosition());

        // Lưu staffInfo vào database
        staffRepository.save(staffInfo);

        return savedUser;
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
