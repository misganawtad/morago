package com.example.morago_backend_nov24.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TranslatorEarningsResponse {
    private Long totalEarnings;
    private Long todayEarnings;
    private Integer totalCalls;
    private Long averagePerCall;
}