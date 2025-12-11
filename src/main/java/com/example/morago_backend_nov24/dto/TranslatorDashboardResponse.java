package com.example.morago_backend_nov24.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TranslatorDashboardResponse {
    private Long balance;
    private Long totalEarnings;
    private Integer totalCalls;
    private Integer pendingWithdrawals;
}