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
