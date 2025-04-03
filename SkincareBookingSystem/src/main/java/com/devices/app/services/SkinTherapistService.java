package com.devices.app.services;

import com.devices.app.dtos.SkinTherapistDto;
import com.devices.app.models.SkinTherapist;
import com.devices.app.repository.SkinTherapistRepository;
import jakarta.persistence.Tuple;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SkinTherapistService {
    final private SkinTherapistRepository skinTherapistRepository;

    public SkinTherapistService(SkinTherapistRepository skinTherapistRepository) {
        this.skinTherapistRepository = skinTherapistRepository;
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
    public List<SkinTherapist> getAllSkinTherapist() {
        return skinTherapistRepository.findAll();
    }
}
