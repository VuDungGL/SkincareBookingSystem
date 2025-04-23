package com.devices.app.services;
import com.devices.app.dtos.dto.FeedBackDto;
import com.devices.app.dtos.dto.HistoriesDto;
import com.devices.app.dtos.response.ApiResponse;
import com.devices.app.models.Feedback;
import com.devices.app.repository.FeedbackRepository;
import jakarta.persistence.Tuple;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Time;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FeedbackService {
    private final FeedbackRepository feedbackRepository;

    public FeedbackService(FeedbackRepository feedbackRepository) {
        this.feedbackRepository = feedbackRepository;
    }

    public ApiResponse<List<FeedBackDto>> getListGoodFeedback() {
        List<FeedBackDto> dtoList = feedbackRepository.getListGoodFeedback().stream()
                .map(tuple -> new FeedBackDto(
                        Optional.ofNullable(tuple.get("FeedbackID", Integer.class)).orElse(0),
                        Optional.ofNullable(tuple.get("Rating", Integer.class)).orElse(0),
                        Optional.ofNullable(tuple.get("Comment", String.class)).orElse(""),
                        Optional.ofNullable(tuple.get("CreateDate", String.class))
                                .map(str -> OffsetDateTime.parse(str.replace(" ", "T") + "Z"))
                                .orElse(null),
                        Optional.ofNullable(tuple.get("FullName", String.class)).orElse(""),
                        Optional.ofNullable(tuple.get("Avt", String.class)).orElse(""),
                        Optional.ofNullable(tuple.get("Gender", Integer.class)).orElse(0),
                        Optional.ofNullable(tuple.get("ServiceName", String.class)).orElse("")
                ))
                .collect(Collectors.toList());

        return new ApiResponse<>(200, "Thành công", dtoList);
    }

    public Page<FeedBackDto> getPageFeedback(String search, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        try {
            Page<Tuple> results = feedbackRepository.getAllPageFeedback(search,pageable);

            if (results.isEmpty()) {
                return Page.empty(pageable);
            }
            List<FeedBackDto> dtoList = results.stream().map(tuple -> new FeedBackDto(
                    Optional.ofNullable(tuple.get("FeedbackID", Integer.class)).orElse(0),
                    Optional.ofNullable(tuple.get("Rating", Integer.class)).orElse(0),
                    Optional.ofNullable(tuple.get("Comment", String.class)).orElse(""),
                    Optional.ofNullable(tuple.get("CreateDate", String.class))
                            .map(str -> OffsetDateTime.parse(str.replace(" ", "T") + "Z"))
                            .orElse(null),
                    Optional.ofNullable(tuple.get("FullName", String.class)).orElse(""),
                    Optional.ofNullable(tuple.get("Avt", String.class)).orElse(""),
                    Optional.ofNullable(tuple.get("Gender", Integer.class)).orElse(0),
                    Optional.ofNullable(tuple.get("ServiceName", String.class)).orElse("")
            )).collect(Collectors.toList());

            return new PageImpl<>(dtoList, pageable, results.getTotalElements());
        } catch (Exception ex) {
            return Page.empty(pageable);
        }
    }
}
