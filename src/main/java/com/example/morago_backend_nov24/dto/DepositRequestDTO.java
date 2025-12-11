package com.example.morago_backend_nov24.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class DepositRequestDTO {
    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    private Long amount;

    private String comment;
}