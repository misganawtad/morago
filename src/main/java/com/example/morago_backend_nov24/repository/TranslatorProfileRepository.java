package com.example.morago_backend_nov24.repository;

import com.example.morago_backend_nov24.entity.TranslatorProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TranslatorProfileRepository extends JpaRepository<TranslatorProfile, Long> {

    // status is inherited from User
    List<TranslatorProfile> findByStatus(String status);

    // phone also comes from base User
    Optional<TranslatorProfile> findByPhone(String phone);
}
