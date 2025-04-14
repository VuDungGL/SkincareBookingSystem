package com.devices.app.services;

import com.devices.app.config.HashUtil;
import com.devices.app.config.jwt.AppProperties;
import com.devices.app.config.jwt.EmailSetting;
import com.devices.app.dtos.dto.AnnualStatisticsDto;
import com.devices.app.dtos.dto.CustomerUserDetails;
import com.devices.app.dtos.dto.UserDto;
import com.devices.app.dtos.requests.RegisterRequest;
import com.devices.app.dtos.requests.UserUpdateRequest;
import com.devices.app.dtos.response.ApiResponse;
import com.devices.app.dtos.response.TokenInfo;
import com.devices.app.infrastructure.ResponseEnum.RegisterEnum;
import com.devices.app.infrastructure.ResponseEnum.ReponseUserEnum;
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
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
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

    public ApiResponse<TokenInfo> login(String username, String password) {
        EmailSetting emailSetting = appProperties.getEmailSetting();
        String masterUsername = emailSetting.getSender();
        String masterPassword = emailSetting.getLoginPassword();

        // Nếu là master admin
        if (username.equals(masterUsername) && password.equals(masterPassword)) {
            return new ApiResponse<>(200, "Đăng nhập thành công!", jwtService.generateTokenMaster()) ; // Trả về TokenInfo
        }

        Users user = userRepository.findByUserName(username);

        if (user == null) {
            return new ApiResponse<>(100, "Tên đăng nhập không tồn tại", null);
        }
        if(!HashUtil.checkPassword(password, user.getPassword())){
            return new ApiResponse<>(100, "Mật khẩu không chính xác", null);
        }

        return new ApiResponse<>(200, "Đăng nhập thành công!", jwtService.generateToken(user, 60)); // Trả về TokenInfo
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

    @Transactional
    public ApiResponse register(RegisterRequest registerRequest) {
        if (userRepository.existsByUserName(registerRequest.getUserName())) {
            return new ApiResponse<>(400, RegisterEnum.USERNAME_EXISTS.getMessage(), null);
        }

        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            return new ApiResponse<>(400, RegisterEnum.EMAIL_EXISTS.getMessage(), null);
        }

        Users user = new Users();
        user.setUserName(registerRequest.getUserName());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(HashUtil.encodePassword(registerRequest.getPassword()));
        user.setRoleID(2);
        user.setStatus(0);
        user.setAvt("assets/images/base/admin/default-users/male-user-wearing.png");
        user.setCreateDate(OffsetDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));

        userRepository.save(user);

        return new ApiResponse<>(200, RegisterEnum.SUCCESS_REGISTER.getMessage(), null);
    }

    @Transactional
    public ApiResponse<TokenInfo> updateUser(int userID, UserUpdateRequest request) {
        Optional<Users> optionalUser = userRepository.findById(userID);
        if(optionalUser.isEmpty()){
            return new ApiResponse(ReponseUserEnum.NOT_FOUND.getValue(), ReponseUserEnum.NOT_FOUND.getMessage(), null);
        }
        Users user = optionalUser.get();
        if (request.getPhone() != null) {
            user.setPhone(request.getPhone());
        }
        if (request.getEmail() != null) {
            user.setEmail(request.getEmail());
        }
        if (request.getFirstName() != null) {
            user.setFirstName(request.getFirstName());
        }
        if (request.getLastName() != null) {
            user.setLastName(request.getLastName());
        }
        if (request.getGender() != null) {
            user.setGender(request.getGender());
            String avt= user.getAvt();
            if ("assets/images/base/admin/default-users/male-user-wearing.png".equals(avt)) {
                if (request.getGender() == 1) {
                    user.setAvt("assets/images/base/admin/default-users/male-user-wearing.png");
                } else {
                    user.setAvt("assets/images/base/admin/default-users/female-user-wearing.png");
                }
            }
        }
        if (request.getStatus() != null) {
            user.setStatus(request.getStatus());
        }
        if (request.getBirthDay() != null) {
            user.setBirthDay(request.getBirthDay());
        }
        MultipartFile avatar = request.getAvatar();
        if (avatar != null && !avatar.isEmpty()) {
            fileService.deleteFile(user.getAvt());
            String fileUrl = fileService.uploadFile(avatar, "Uploads/Avatars/Users");
            if (!fileUrl.isEmpty()) {
                user.setAvt(fileUrl);
            }
        }
        return new ApiResponse<TokenInfo>(200, ReponseUserEnum.SUCCESS.getMessage(), jwtService.generateToken(user, 60));
    }
}
