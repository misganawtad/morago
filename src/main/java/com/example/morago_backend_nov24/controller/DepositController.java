package com.example.morago_backend_nov24.controller;

import com.example.morago_backend_nov24.dto.DepositRequestDTO;
import com.example.morago_backend_nov24.dto.DepositResponse;
import com.example.morago_backend_nov24.security.CustomUserDetails;
import com.example.morago_backend_nov24.service.DepositService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/deposits")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class DepositController {

    private final DepositService depositService;

    @PostMapping("/request")
    public DepositResponse requestDeposit(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody DepositRequestDTO request) {
        return depositService.requestDeposit(
                userDetails.getId(),
                request.getAmount(),
                request.getComment()
        );
    }

    @GetMapping("/my-deposits")
    public List<DepositResponse> getMyDeposits(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return depositService.getMyDeposits(userDetails.getId());
    }
}