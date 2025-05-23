package com.devices.app.controller.admin;

import com.devices.app.dtos.dto.SkinTherapistDto;
import com.devices.app.dtos.requests.TherapistCreationRequest;
import com.devices.app.models.SkinTherapist;
import com.devices.app.services.SkinTherapistService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
public class SkinTherapistAdminController {
    private final SkinTherapistService skinTherapistService;

    public SkinTherapistAdminController(SkinTherapistService skinTherapistService) {
        this.skinTherapistService = skinTherapistService;
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MASTER_ADMIN')")
    @PostMapping("/therapist/getListSkinTherapist")
    public Page<SkinTherapistDto> getListSkinTherapist(@RequestBody Map<String, Object> request) {
        int pageIndex = (int) request.getOrDefault("pageIndex", 0);
        int pageSize = (int) request.getOrDefault("pageSize", 6);
        String searchText = (String) request.getOrDefault("searchText", "");

        if (pageIndex < 0) pageIndex = 0;
        if (pageSize <= 0) pageSize = 6;
        return skinTherapistService.getListSkinTherapist(searchText,pageIndex,pageSize);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MASTER_ADMIN')")
    @GetMapping("/therapist/getAllSkinTherapist")
    public List<SkinTherapist> getAllSkinTherapist() {
        return skinTherapistService.getAllSkinTherapist();
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MASTER_ADMIN')")
    @PostMapping("/therapist/deleteSkinTherapist")
    public ResponseEntity<String> deleteSkinTherapist(@RequestBody Map<String, Integer> request) {
        int skinTherapistID = request.get("skinTherapistID");
        String result = skinTherapistService.deleteSkinTherapist(skinTherapistID);
        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MASTER_ADMIN')")
    @PostMapping("/therapist/getSkinTherapistById")
    public SkinTherapist getSkinTherapistById(@RequestBody Map<String, Integer> request) {
        int skinTherapistID = request.get("skinTherapistID");
        return skinTherapistService.getSkinTherapistById(skinTherapistID);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MASTER_ADMIN')")
    @PostMapping("/therapist/updateTherapist")
    public ResponseEntity<SkinTherapist> updateTherapist(
            @RequestParam("skinTherapistID") Integer skinTherapistID,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "firstName", required = false) String firstName,
            @RequestParam(value = "lastName", required = false) String lastName,
            @RequestParam(value = "phone", required = false) String phone,
            @RequestParam(value = "gender", required = false) Integer gender,
            @RequestParam(value = "expertise", required = false) String expertise,
            @RequestParam(value = "experience", required = false) Integer experience,
            @RequestParam(value = "salary", required = false) Integer salary,
            @RequestParam(value = "status", required = false) Integer status,
            @RequestParam(value = "birthDate", required = false) OffsetDateTime birthDate,
            @RequestParam(value = "avatar", required = false) MultipartFile avatar) {

        // Tạo đối tượng request từ các tham số gửi lên
        TherapistCreationRequest request = new TherapistCreationRequest();
        request.setSkinTherapistID(skinTherapistID);
        request.setEmail(email);
        request.setFirstName(firstName);
        request.setLastName(lastName);
        request.setPhone(phone);
        request.setGender(gender);
        request.setExpertise(expertise);
        request.setExperience(experience);
        request.setSalary(salary);
        request.setStatus(status);
        request.setAvatar(avatar);
        request.setBirthDate(birthDate);


        SkinTherapist updatedTherapist = skinTherapistService.updateTherapist(request);
        return ResponseEntity.ok(updatedTherapist);

    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MASTER_ADMIN')")
    @PostMapping("/therapist/createTherapist")
    public ResponseEntity<SkinTherapist> createTherapist(
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "firstName", required = false) String firstName,
            @RequestParam(value = "lastName", required = false) String lastName,
            @RequestParam(value = "phone", required = false) String phone,
            @RequestParam(value = "gender", required = false) Integer gender,
            @RequestParam(value = "expertise", required = false) String expertise,
            @RequestParam(value = "experience", required = false) Integer experience,
            @RequestParam(value = "salary", required = false) Integer salary,
            @RequestParam(value = "status", required = false) Integer status,
            @RequestParam(value = "birthDate", required = false) OffsetDateTime birthDate,
            @RequestParam(value = "avatar", required = false) MultipartFile avatar) {

        TherapistCreationRequest request = new TherapistCreationRequest();
        request.setEmail(email);
        request.setFirstName(firstName);
        request.setLastName(lastName);
        request.setPhone(phone);
        request.setGender(gender);
        request.setExpertise(expertise);
        request.setExperience(experience);
        request.setSalary(salary);
        request.setStatus(status);
        request.setAvatar(avatar);
        request.setBirthDate(birthDate);


        SkinTherapist updatedTherapist = skinTherapistService.createTherapist(request);
        return ResponseEntity.ok(updatedTherapist);

    }
}
