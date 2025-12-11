package com.example.morago_backend_nov24.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessagingService {

    private final SmsService smsService;

    public void sendVerificationCode(String phone, String code, String purpose) {
        log.info("Sending {} verification code to {}", purpose, phone);
        smsService.sendSignupCode(phone, code);
    }

    public void sendPasswordResetCode(String phone, String code) {
        log.info("Sending password reset code to {}", phone);
        smsService.sendSignupCode(phone, code);
    }
}

