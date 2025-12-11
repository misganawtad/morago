package com.example.morago_backend_nov24.dto;

import com.example.morago_backend_nov24.entity.Deposit;
import lombok.Data;
import java.time.LocalDateTime;
@Data
public class DepositResponse {
    private Long id;
    private Long amount;
    private String status;
    private String comment;
    private LocalDateTime createdAt;
    private LocalDateTime completedAt;

    // User info
    private Long userId;
    private String userName;
    private Long userBalance;

    public static DepositResponse from(Deposit deposit) {
        DepositResponse dto = new DepositResponse();
        dto.setId(deposit.getId());
        dto.setAmount(deposit.getAmount());
        dto.setStatus(deposit.getStatus());
        dto.setComment(deposit.getComment());
        dto.setCreatedAt(deposit.getCreatedAt());
        dto.setCompletedAt(deposit.getCompletedAt());

        // Only include needed user fields
        dto.setUserId(deposit.getUserProfile().getId());
        dto.setUserName(deposit.getUserProfile().getName());
        dto.setUserBalance(deposit.getUserProfile().getBalance());

        return dto;
    }
}