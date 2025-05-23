package com.devices.app.services;

import com.devices.app.dtos.dto.SkinTherapistDto;
import com.devices.app.dtos.requests.TherapistCreationRequest;
import com.devices.app.dtos.response.ApiResponse;
import com.devices.app.infrastructure.ResponseEnum.RegisterEnum;
import com.devices.app.infrastructure.ResponseEnum.TherapistEnum;
import com.devices.app.models.SkinTherapist;
import com.devices.app.repository.SkinTherapistRepository;
import jakarta.persistence.Tuple;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SkinTherapistService {

    final private SkinTherapistRepository skinTherapistRepository;
    final private FileService fileService;

    public SkinTherapistService(SkinTherapistRepository skinTherapistRepository, FileService fileService) {
        this.skinTherapistRepository = skinTherapistRepository;
        this.fileService = fileService;
    }


    public Page<SkinTherapistDto> getListSkinTherapist(String search, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        try {
            Page<Tuple> results = skinTherapistRepository.getListSkinTherapist(search,pageable);

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

    public List<SkinTherapistDto> getAllTherapists() {
        List<SkinTherapist> entities = skinTherapistRepository.findAll();
        List<SkinTherapistDto> dtoList = new ArrayList<>();

        for (SkinTherapist therapist : entities) {
            SkinTherapistDto dto = new SkinTherapistDto(
                    therapist.getId() != null ? therapist.getId() : 0,
                    therapist.getEmail() != null ? therapist.getEmail() : "",
                    therapist.getFirstName() != null ? therapist.getFirstName() : "",
                    therapist.getLastName() != null ? therapist.getLastName() : "",
                    therapist.getPhone() != null ? therapist.getPhone() : "",
                    therapist.getBirthDate() != null ? therapist.getBirthDate() : OffsetDateTime.from(LocalDate.of(2000, 1, 1)),
                    therapist.getAvt() != null ? therapist.getAvt() : "/assets/images/base/admin/default-users/female-employee-wearing.png",
                    therapist.getGender() != null ? therapist.getGender() : 0,
                    therapist.getExpertise() != null ? therapist.getExpertise() : "Chưa cập nhật",
                    therapist.getExperience() != null ? therapist.getExperience() : 0,
                    therapist.getSalary() != null ? therapist.getSalary() : 0,
                    therapist.getStatus() != null ? therapist.getStatus() : 0
            );
            dtoList.add(dto);
        }


        return dtoList;
    }




    public List<SkinTherapist> getAllSkinTherapist() {
        return skinTherapistRepository.findAll();
    }

    @Transactional
    public String deleteSkinTherapist(int id) {
        try{
            Optional<SkinTherapist> optionalUser = skinTherapistRepository.findById(id);
            if (optionalUser.isPresent()) {
                SkinTherapist therapist = optionalUser.get();
                String avatarPath = therapist.getAvt();

                if (avatarPath != null && !avatarPath.isEmpty()) {
                    fileService.deleteFile(avatarPath);
                }

                skinTherapistRepository.deleteById(id);
                return "Xóa thành công";
            } else {
                return "Nhân viên không tồn tại";
            }
        }
        catch (Exception ex) {
            return "Xóa nhân viên thất bại!";
        }
    }

    public SkinTherapist getSkinTherapistById(int id) {
        return skinTherapistRepository.findById(id).orElse(new SkinTherapist());
    }

    @Transactional
    public SkinTherapist updateTherapist(@Valid TherapistCreationRequest request) {
        Optional<SkinTherapist> optionalTherapist = skinTherapistRepository.findById(request.getSkinTherapistID());
        if (optionalTherapist.isEmpty()) {
            throw new RuntimeException("Therapist không tồn tại!");
        }

        SkinTherapist therapist = optionalTherapist.get();

        if (request.getEmail() != null) {
            therapist.setEmail(request.getEmail());
        }
        if (request.getFirstName() != null) {
            therapist.setFirstName(request.getFirstName());
        }
        if (request.getLastName() != null) {
            therapist.setLastName(request.getLastName());
        }
        if (request.getPhone() != null) {
            therapist.setPhone(request.getPhone());
        }
        if (request.getGender() != null) {
            therapist.setGender(request.getGender());
        }
        if (request.getExpertise() != null) {
            therapist.setExpertise(request.getExpertise());
        }
        if (request.getExperience() != null) {
            therapist.setExperience(request.getExperience());
        }
        if (request.getSalary() != null) {
            therapist.setSalary(request.getSalary());
        }
        if (request.getStatus() != null) {
            therapist.setStatus(request.getStatus());
        }
        if (request.getBirthDate() != null) {
            therapist.setBirthDate(request.getBirthDate());
        }

        MultipartFile avatar = request.getAvatar();
        if (avatar != null && !avatar.isEmpty()) {
            fileService.deleteFile(therapist.getAvt());
            String fileUrl = fileService.uploadFile(avatar, "Uploads/Avatars/Therapists");
            if (!fileUrl.isEmpty()) {
                therapist.setAvt(fileUrl);
            }
        }

        return skinTherapistRepository.save(therapist);
    }
    @Transactional
    public SkinTherapist createTherapist(TherapistCreationRequest request) {

        SkinTherapist therapist = new SkinTherapist();

        therapist.setEmail(request.getEmail());
        therapist.setFirstName(request.getFirstName());
        therapist.setLastName(request.getLastName());
        therapist.setPhone(request.getPhone());
        therapist.setGender(request.getGender());
        therapist.setExpertise(request.getExpertise());
        therapist.setExperience(request.getExperience());
        therapist.setSalary(request.getSalary());
        therapist.setStatus(request.getStatus());
        therapist.setBirthDate(request.getBirthDate());

        MultipartFile avatar = request.getAvatar();
        if (avatar != null && !avatar.isEmpty()) {
            String fileUrl = fileService.uploadFile(avatar, "Uploads/Avatars/Therapists");
            if (!fileUrl.isEmpty()) {
                therapist.setAvt(fileUrl);
            }
        }

        return skinTherapistRepository.save(therapist);
    }

    public List<SkinTherapist> getListSkinTherapistNotBusy(LocalDate workDate, LocalTime startTime, LocalTime endTime) {
        return skinTherapistRepository.getListSkinTherapistNotBusy(workDate, startTime, endTime);
    }
}
