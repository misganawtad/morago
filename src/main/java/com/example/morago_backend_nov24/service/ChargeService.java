package com.example.morago_backend_nov24.service;

import com.example.morago_backend_nov24.dto.ChargeRequest;
import com.example.morago_backend_nov24.dto.ChargeResponse;
import com.example.morago_backend_nov24.entity.Charge;
import com.example.morago_backend_nov24.entity.UserProfile;
import com.example.morago_backend_nov24.repository.ChargeRepository;
import com.example.morago_backend_nov24.repository.UserProfileRepository;
import com.example.morago_backend_nov24.mapper.ChargeMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChargeService {

    private final ChargeRepository chargeRepository;
    private final UserProfileRepository userProfileRepository;
    private final ChargeMapper chargeMapper;

    @Transactional
    public Charge createCharge(Long userId, Long amount, String label) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than 0");
        }

        UserProfile user = userProfileRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));


        Charge charge = new Charge();
        charge.setUserProfile(user);
        charge.setUserName(user.getName()); // Snapshot for audit
        charge.setLabel(label);
        charge.setAmount(amount);
        charge.setCreatedAt(LocalDateTime.now());

        Charge saved = chargeRepository.save(charge);


        Long currentBalance = user.getBalance() != null ? user.getBalance() : 0L;
        user.setBalance(currentBalance + amount);
        userProfileRepository.save(user);

        log.info("Admin created charge for user {}. Amount: {} KRW, Label: {}, New balance: {} KRW",
                userId, amount, label, user.getBalance());

        return saved;
    }

    @Transactional
    public ChargeResponse createCharge(ChargeRequest request) {
        Charge saved = createCharge(
                request.getUserId(),
                request.getAmount(),
                request.getLabel()
        );
        return chargeMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<ChargeResponse> getAllCharges() {
        return chargeRepository.findAll()
                .stream()
                .map(chargeMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ChargeResponse> getUserCharges(Long userId) {
        return chargeRepository.findByUserProfileIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(chargeMapper::toResponse)
                .toList();
    }
}