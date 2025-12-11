package com.example.morago_backend_nov24.dto;

import com.example.morago_backend_nov24.entity.Charge;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class ChargeResponse {
    private Long id;
    private Long userId;
    private String userName;
    private String label;
    private Long amount;
    private LocalDateTime createdAt;

    public static ChargeResponse from(Charge charge) {
        return ChargeResponse.builder()
                .id(charge.getId())
                .userId(charge.getUserProfile().getId())
                .userName(charge.getUserName())
                .label(charge.getLabel())
                .amount(charge.getAmount())
                .createdAt(charge.getCreatedAt())
                .build();
    }
}