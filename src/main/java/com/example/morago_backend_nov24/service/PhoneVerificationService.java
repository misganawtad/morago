package com.example.morago_backend_nov24.service;

import com.example.morago_backend_nov24.dto.SignupCodeRequest;
import com.example.morago_backend_nov24.entity.PhoneVerification;
import com.example.morago_backend_nov24.enums.VerificationType;
import com.example.morago_backend_nov24.repository.PhoneVerificationRepository;
import com.example.morago_backend_nov24.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class PhoneVerificationService {

    private final PhoneVerificationRepository phoneVerificationRepository;
    private final UserRepository userRepository;
    private final MessagingService messagingService;

    @Transactional
    public void sendSignupCode(SignupCodeRequest request) {
        String phone = request.getPhone();

        if (userRepository.existsByPhone(phone)) {
            throw new IllegalArgumentException("Phone already registered");
        }
        String code = generateCode();
        PhoneVerification verification = PhoneVerification.builder()
                .phone(phone)
                .code(code)
                .type(VerificationType.SIGNUP) // ← changed
                .expiresAt(LocalDateTime.now().plusMinutes(5))
                .used(false)
                .build();

        phoneVerificationRepository.save(verification);
        messagingService.sendVerificationCode(phone, code, "SIGNUP");
    }

    public void validateSignupCode(String phone, String codeFromUser) {
        PhoneVerification verification = phoneVerificationRepository
                .findTopByPhoneAndTypeOrderByIdDesc(phone, VerificationType.SIGNUP) // ← changed
                .orElseThrow(() ->
                        new IllegalArgumentException("No verification code sent for this phone")
                );
        if (verification.isUsed()) {
            throw new IllegalArgumentException("Verification code already used");
        }

        if (verification.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Verification code expired");
        }

        if (!verification.getCode().equals(codeFromUser)) {
            throw new IllegalArgumentException("Invalid verification code");
        }

        verification.setUsed(true);
        phoneVerificationRepository.save(verification);
    }

    private String generateCode() {
        return String.valueOf(new Random().nextInt(900000) + 100000);
    }
}
