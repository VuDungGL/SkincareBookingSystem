package com.devices.app.services;

import com.devices.app.dtos.AnnualStatisticsDto;
import com.devices.app.dtos.SkinTherapistDto;
import com.devices.app.models.SkinTherapist;
import com.devices.app.models.Users;
import jakarta.persistence.Tuple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.devices.app.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
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

    @Autowired
    public UserService(UserRepository userRepository, FileService fileService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.fileService = fileService;
        this.passwordEncoder = passwordEncoder;
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


    public Page<SkinTherapistDto> getListSkinTherapist(String search, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        try {
            Page<Tuple> results = userRepository.getListSkinTherapist(search,pageable);

            if (results.isEmpty()) {
                return Page.empty(pageable);
            }

            List<SkinTherapistDto> dtoList = results.stream().map(tuple -> new SkinTherapistDto(
                    Optional.ofNullable(tuple.get("skinTherapistID", Integer.class)).orElse(0),
                    Optional.ofNullable(tuple.get("email", String.class)).orElse(""),
                    Optional.ofNullable(tuple.get("firstName", String.class)).orElse(""),
                    Optional.ofNullable(tuple.get("lastName", String.class)).orElse(""),
                    Optional.ofNullable(tuple.get("phone", String.class)).orElse(""),
                    Optional.ofNullable(tuple.get("birthDate", String.class))
                            .map(str -> LocalDateTime.parse(str, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                                    .atOffset(ZoneOffset.UTC))
                            .orElse(null),
                    Optional.ofNullable(tuple.get("avt", String.class)).orElse(""),
                    Optional.ofNullable(tuple.get("gender", Integer.class)).orElse(0),
                    Optional.ofNullable(tuple.get("expertise", String.class)).orElse(""),
                    Optional.ofNullable(tuple.get("experience", Integer.class)).orElse(0),
                    Optional.ofNullable(tuple.get("salary", Integer.class)).orElse(0),
                    Optional.ofNullable(tuple.get("status", Integer.class)).orElse(0)
            )).collect(Collectors.toList());
            return new PageImpl<>(dtoList, pageable, results.getTotalElements());
        } catch (Exception ex) {
            return Page.empty(pageable);
        }
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
}
