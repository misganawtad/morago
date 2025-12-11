package com.example.morago_backend_nov24.dto;

import lombok.Data;

@Data
public class DepositConfirmRequest {
    private Long amount;  // Admin can adjust amount
    private String label; // Admin can add label
}