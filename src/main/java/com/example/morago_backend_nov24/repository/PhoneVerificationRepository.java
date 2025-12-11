package com.example.morago_backend_nov24.repository;

import com.example.morago_backend_nov24.entity.PhoneVerification;
import com.example.morago_backend_nov24.enums.VerificationType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PhoneVerificationRepository extends JpaRepository<PhoneVerification, Long> {


    Optional<PhoneVerification> findTopByPhoneAndTypeOrderByIdDesc(String phone, VerificationType type);
    Optional<PhoneVerification> findTopByPhoneAndCodeAndTypeAndUsedFalseOrderByIdDesc(
            String phone,
            String code,
            VerificationType type
    );
}

