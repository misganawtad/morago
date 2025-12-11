package com.example.morago_backend_nov24.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SmsService {

    public void sendSignupCode(String phone, String code) {
        // TODO: replace with real SMS provider
        log.info("📱 Sending signup SMS to {} with code: {}", phone, code);
    }
}
