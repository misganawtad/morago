package com.example.morago_backend_nov24.dto;

import com.example.morago_backend_nov24.entity.Withdrawal;
import com.example.morago_backend_nov24.enums.WithdrawalStatus;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class WithdrawalResponse {
    private Long id;
    private Long translatorId;
    private String translatorName;
    private String bankName;
    private String bankAccountMasked;
    private Long amount;
    private WithdrawalStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime completedAt;

    public static WithdrawalResponse from(Withdrawal withdrawal) {
        return WithdrawalResponse.builder()
                .id(withdrawal.getId())
                .translatorId(withdrawal.getTranslatorProfile().getId())
                .translatorName(withdrawal.getTranslatorProfile().getFirstName() + " " +
                        withdrawal.getTranslatorProfile().getLastName())
                .bankName(withdrawal.getBankName())
                .bankAccountMasked(maskBankAccount(withdrawal.getBankAccount()))
                .amount(withdrawal.getAmount())
                .status(withdrawal.getStatus())
                .createdAt(withdrawal.getCreatedAt())
                .completedAt(withdrawal.getCompletedAt())
                .build();
    }

    public static WithdrawalResponse fromForAdmin(Withdrawal withdrawal) {
        WithdrawalResponse response = from(withdrawal);
        response.setBankAccountMasked(withdrawal.getBankAccount());
        return response;
    }

    private static String maskBankAccount(String account) {
        if (account == null || account.length() <= 4) {
            return "****";
        }
        return "****" + account.substring(account.length() - 4);
    }
}