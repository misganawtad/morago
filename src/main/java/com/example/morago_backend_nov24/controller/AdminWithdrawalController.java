package com.example.morago_backend_nov24.controller;

import com.example.morago_backend_nov24.dto.WithdrawalResponse;
import com.example.morago_backend_nov24.service.WithdrawalService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/withdrawals")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class AdminWithdrawalController {

    private final WithdrawalService withdrawalService;

    @GetMapping("/pending")
    public List<WithdrawalResponse> getPendingWithdrawals() {
       return withdrawalService.getAdminPendingWithdrawals();
    }

    @PostMapping("/{withdrawalId}/approve")
    public void approveWithdrawal(@PathVariable Long withdrawalId) {
        withdrawalService.approveWithdrawal(withdrawalId);
    }

    @PostMapping("/{withdrawalId}/reject")
    public void rejectWithdrawal(@PathVariable Long withdrawalId,
                                 @RequestParam String reason) {
        withdrawalService.rejectWithdrawal(withdrawalId, reason);
    }

    @GetMapping
    public List<WithdrawalResponse> getAllWithdrawals() {
        // admin version: full bank info
        return withdrawalService.getAdminAllWithdrawals();
    }
}
