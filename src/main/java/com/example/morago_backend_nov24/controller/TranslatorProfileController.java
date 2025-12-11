package com.example.morago_backend_nov24.controller;

import com.example.morago_backend_nov24.dto.*;

import com.example.morago_backend_nov24.security.CustomUserDetails;
import com.example.morago_backend_nov24.service.TranslatorProfileService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/translator")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class TranslatorProfileController {

    private final TranslatorProfileService translatorProfileService;


    @PutMapping("/me/profile")
    public void completeProfile(@Valid @RequestBody TranslatorProfileCompletionRequest request,
                                @AuthenticationPrincipal CustomUserDetails userDetails) {
        translatorProfileService.completeProfile(userDetails.getId(), request);
    }

    @GetMapping("/dashboard")
    public TranslatorDashboardResponse getDashboard(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return translatorProfileService.getDashboard(userDetails.getId());
    }

    @GetMapping("/balance")
    public TranslatorBalanceResponse getBalance(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return translatorProfileService.getBalance(userDetails.getId());
    }

    @GetMapping("/calls")
    public List<CallResponse> getMyCalls(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(required = false) String status) {
        return translatorProfileService.getCalls(userDetails.getId(), status);
    }

    @GetMapping("/earnings")
    public TranslatorEarningsResponse getEarnings(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return translatorProfileService.getEarnings(userDetails.getId());
    }

    @PostMapping("/withdrawals/request")
    public WithdrawalResponse requestWithdrawal(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody WithdrawalRequest request) {
        return translatorProfileService.requestWithdrawal(userDetails.getId(), request);
    }

    @GetMapping("/withdrawals")
    public List<WithdrawalResponse> getMyWithdrawals(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return translatorProfileService.getWithdrawals(userDetails.getId());
    }

    @GetMapping("/withdrawals/pending")
    public List<WithdrawalResponse> getPendingWithdrawals(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return translatorProfileService.getPendingWithdrawals(userDetails.getId());
    }
}