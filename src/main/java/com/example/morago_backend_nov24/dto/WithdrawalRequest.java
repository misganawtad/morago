package com.example.morago_backend_nov24.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class WithdrawalRequest {
    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    private Long amount;

    @NotBlank(message = "Bank name is required")
    private String bankName;

    @NotBlank(message = "Bank account is required")
    private String bankAccount;
}