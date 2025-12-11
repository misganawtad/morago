package com.example.morago_backend_nov24.dto;

import com.example.morago_backend_nov24.entity.Rating;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class RatingResponse {

    private Long id;
    private Long callId;
    private Long ratedTranslatorId;
    private Long ratedByUserId;
    private Integer score;
    private LocalDateTime createdAt;

    public static RatingResponse from(Rating rating) {
        return RatingResponse.builder()
                .id(rating.getId())
                .callId(rating.getCall().getId())
                .ratedTranslatorId(rating.getRatedTranslator().getId())
                .ratedByUserId(rating.getRatedByUser().getId())
                .score(rating.getScore())
                .createdAt(rating.getCreatedAt())
                .build();
    }
}
