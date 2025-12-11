package com.example.morago_backend_nov24.controller;

import com.example.morago_backend_nov24.dto.DepositResponse;
import com.example.morago_backend_nov24.service.DepositService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/deposits")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@PreAuthorize("hasRole('ADMIN')")
public class AdminDepositController {

    private final DepositService depositService;

    @GetMapping("/pending")
    public List<DepositResponse> getPendingDeposits() {
        return depositService.getPendingDeposits();
    }

    @PostMapping("/{depositId}/approve")
    public void approveDeposit(@PathVariable Long depositId) {
        depositService.approveDeposit(depositId);
    }

    @PostMapping("/{depositId}/reject")
    public void rejectDeposit(@PathVariable Long depositId,
                              @RequestParam String reason) {
        depositService.rejectDeposit(depositId, reason);
    }
}

