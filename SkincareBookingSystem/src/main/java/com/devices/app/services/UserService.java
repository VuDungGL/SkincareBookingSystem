package com.devices.app.services;

import com.devices.app.config.HashUtil;
import com.devices.app.config.jwt.AppProperties;
import com.devices.app.config.jwt.EmailSetting;
import com.devices.app.dtos.dto.AnnualStatisticsDto;
import com.devices.app.dtos.dto.AuthenticationDto;
import com.devices.app.dtos.dto.CustomerUserDetails;
import com.devices.app.dtos.dto.UserDto;
import com.devices.app.dtos.response.TokenInfo;
import com.devices.app.infrastructure.userEnum.UserRoleEnum;
import com.devices.app.models.Users;
import jakarta.persistence.Tuple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final FileService fileService;
    private final JWTService jwtService;
    private final AppProperties appProperties;


    @Autowired
    public UserService(UserRepository userRepository, FileService fileService, JWTService jwtService, AppProperties appProperties ) {
        this.userRepository = userRepository;
        this.fileService = fileService;
        this.jwtService = jwtService;
        this.appProperties = appProperties;
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

    @Transactional
    public String deleteUser(int id) {
        try{
            Optional<Users> optionalUser = userRepository.findById(id);
            if (optionalUser.isPresent()) {
                Users user = optionalUser.get();
                String avatarPath = user.getAvt();

                if (avatarPath != null && !avatarPath.isEmpty()) {
                    fileService.deleteFile(avatarPath);
                }

                userRepository.deleteById(id);
                return "Xóa thành công";
            } else {
                return "Nhân viên không tồn tại";
            }
        }
        catch (Exception ex) {
            return "Xóa nhân viên thất bại!";
        }

    }

    public List<Users> getListCustomer(int status) {
        return userRepository.findAllByStatus(status);
    }

    public Page<UserDto> getListCustomerByUserRole(String searchText, int roleID, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        try {
            Page<Tuple> results = userRepository.findAllUserByUserRole(searchText,roleID, pageable);

            if (results.isEmpty()) {
                return Page.empty(pageable);
            }

            List<UserDto> dtoList = results.stream().map(tuple -> new UserDto(
                    Optional.ofNullable(tuple.get("userID", Integer.class)).orElse(0),
                    Optional.ofNullable(tuple.get("userName", String.class)).orElse(""),
                    Optional.ofNullable(tuple.get("roleID", Integer.class)).orElse(0),
                    Optional.ofNullable(tuple.get("email", String.class)).orElse(""),
                    Optional.ofNullable(tuple.get("phone", String.class)).orElse(""),
                    Optional.ofNullable(tuple.get("firstName", String.class)).orElse(""),
                    Optional.ofNullable(tuple.get("lastName", String.class)).orElse(""),
                    Optional.ofNullable(tuple.get("avt", String.class)).orElse(""),
                    Optional.ofNullable(tuple.get("gender", Integer.class)).orElse(0),
                    Optional.ofNullable(tuple.get("birthDate", String.class))
                            .map(str -> LocalDateTime.parse(str, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                                    .atOffset(ZoneOffset.UTC))
                            .orElse(null),
                    Optional.ofNullable(tuple.get("status", Integer.class)).orElse(0)
            )).collect(Collectors.toList());
            return new PageImpl<>(dtoList, pageable, results.getTotalElements());
        } catch (Exception ex) {
            return Page.empty(pageable);
        }
    }

    public TokenInfo login(String username, String password) {
        EmailSetting emailSetting = appProperties.getEmailSetting();
        String masterUsername = emailSetting.getSender();
        String masterPassword = emailSetting.getLoginPassword();

        // Nếu là master admin
        if (username.equals(masterUsername) && password.equals(masterPassword)) {
            return jwtService.generateTokenMaster(); // Trả về TokenInfo
        }

        Users user = userRepository.findByUserName(username);

        if (user == null || !HashUtil.checkPassword(password, user.getPassword())) {
            return null;
        }

        return jwtService.generateToken(user, 60); // Trả về TokenInfo
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Kiểm tra xem user có tồn tại trong database không?
        Users user = userRepository.findByUserName(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return new CustomerUserDetails(user);
    }
    public UserDetails loadUserById(Long id) {
        Users user = userRepository.findById(Math.toIntExact(id)).orElse(null);
        if (user == null) return null;
        return new CustomerUserDetails(user);
    }

}
