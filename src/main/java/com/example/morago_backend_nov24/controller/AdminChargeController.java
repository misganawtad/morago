package com.example.morago_backend_nov24.controller;

import com.example.morago_backend_nov24.dto.ChargeRequest;
import com.example.morago_backend_nov24.dto.ChargeResponse;
import com.example.morago_backend_nov24.service.ChargeService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/charges")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class AdminChargeController {

    private final ChargeService chargeService;

    @PostMapping
    public ChargeResponse createCharge(@RequestBody @Valid ChargeRequest request) {
        return chargeService.createCharge(request);
    }

    @GetMapping
    public List<ChargeResponse> getAllCharges() {
        return chargeService.getAllCharges();
    }

    @GetMapping("/user/{userId}")
    public List<ChargeResponse> getUserCharges(@PathVariable Long userId) {
        return chargeService.getUserCharges(userId);
    }
}
