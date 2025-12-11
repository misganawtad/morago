package com.example.morago_backend_nov24.controller;

import com.example.morago_backend_nov24.dto.*;
import com.example.morago_backend_nov24.service.AuthService;
import com.example.morago_backend_nov24.service.PhoneVerificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final PhoneVerificationService phoneVerificationService;

    @PostMapping("/signup/code")
    public void requestSignupCode(@RequestBody @Valid SignupCodeRequest request) {
        phoneVerificationService.sendSignupCode(request);
    }

    @PostMapping("/signup")
    public SignupResponse signup(@RequestBody @Valid SignupRequest request) {
        return authService.signup(request);
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody @Valid LoginRequest request) {
        return authService.login(request);
    }

    @PostMapping("/password/reset/code")
    public void sendResetCode(@RequestBody @Valid PasswordResetCodeRequest request) {
        authService.sendPasswordResetCode(request);
    }

    @PostMapping("/password/reset")
    public void resetPassword(@RequestBody @Valid PasswordResetConfirmRequest request) {
        authService.resetPassword(request);
    }
}