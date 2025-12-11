package com.example.morago_backend_nov24.dto;

import lombok.Data;

@Data
public class ChargeRequest {
    private Long userId;
    private Long amount;
    private String label; // "Welcome bonus", "Compensation", etc.
}
