package com.example.morago_backend_nov24.dto;

import com.example.morago_backend_nov24.entity.Call;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class CallResponse {
    private Long id;

    // User info
    private Long userId;
    private String userName;

    // Translator info
    private Long translatorId;
    private String translatorName;

    // Theme info
    private Long themeId;
    private String themeName;

    // Call details
    private String status;
    private LocalDateTime startedAt;
    private LocalDateTime endedAt;
    private Integer durationSeconds;
    private Long cost;

    public static CallResponse from(Call call) {
        return CallResponse.builder()
                .id(call.getId())
                .userId(call.getUserProfile().getId())
                .userName(call.getUserProfile().getName())
                .translatorId(call.getTranslatorProfile().getId())
                .translatorName(call.getTranslatorProfile().getFirstName() + " " +
                        call.getTranslatorProfile().getLastName())
                .themeId(call.getTheme().getId())
                .themeName(call.getTheme().getName())
                .status(call.getStatus())
                .startedAt(call.getStartedAt())
                .endedAt(call.getEndedAt())
                .durationSeconds(call.getDurationSeconds())
                .cost(call.getSum())
                .build();
    }
}