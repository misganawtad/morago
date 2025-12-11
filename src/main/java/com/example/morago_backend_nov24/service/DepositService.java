package com.example.morago_backend_nov24.service;

import com.example.morago_backend_nov24.dto.DepositResponse;
import com.example.morago_backend_nov24.entity.Deposit;
import com.example.morago_backend_nov24.entity.UserProfile;
import com.example.morago_backend_nov24.mapper.DepositMapper;
import com.example.morago_backend_nov24.repository.DepositRepository;
import com.example.morago_backend_nov24.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DepositService {

    private final DepositRepository depositRepository;
    private final UserProfileRepository userProfileRepository;
    private final DepositMapper depositMapper;

    @Transactional
    public DepositResponse requestDeposit(Long userId, Long amount, String comment) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than 0");
        }

        UserProfile user = userProfileRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Deposit deposit = new Deposit();
        deposit.setUserProfile(user);
        deposit.setAmount(amount);
        deposit.setStatus("PENDING");
        deposit.setComment(comment);
        deposit.setCreatedAt(LocalDateTime.now());
        Deposit saved = depositRepository.save(deposit);
        log.info("User {} requested deposit of {} KRW. Comment: {}", userId, amount, comment);
        return depositMapper.toResponse(saved);
    }

    @Transactional
    public void approveDeposit(Long depositId) {
        Deposit deposit = depositRepository.findById(depositId)
                .orElseThrow(() -> new IllegalArgumentException("Deposit not found"));

        if (!"PENDING".equals(deposit.getStatus())) {
            throw new IllegalArgumentException("Deposit is not pending");
        }

        UserProfile user = deposit.getUserProfile();

        Long currentBalance = user.getBalance() != null ? user.getBalance() : 0L;
        user.setBalance(currentBalance + deposit.getAmount());
        userProfileRepository.save(user);

        deposit.setStatus("COMPLETED");
        deposit.setCompletedAt(LocalDateTime.now());
        depositRepository.save(deposit);

        log.info("Deposit {} approved. User {} balance increased by {} KRW. New balance: {} KRW",
                depositId, user.getId(), deposit.getAmount(), user.getBalance());
    }

    @Transactional
    public void rejectDeposit(Long depositId, String reason) {
        Deposit deposit = depositRepository.findById(depositId)
                .orElseThrow(() -> new IllegalArgumentException("Deposit not found"));

        if (!"PENDING".equals(deposit.getStatus())) {
            throw new IllegalArgumentException("Deposit is not pending");
        }

        deposit.setStatus("REJECTED");
        deposit.setCompletedAt(LocalDateTime.now());
        deposit.setComment(deposit.getComment() + " | REJECTED: " + reason);
        depositRepository.save(deposit);

        log.info("Deposit {} rejected. Reason: {}", depositId, reason);
    }

    @Transactional(readOnly = true)
    public List<DepositResponse> getMyDeposits(Long userId) {
        return depositRepository.findByUserProfileIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(depositMapper::toResponse)
                .collect(Collectors.toList());
    }


    @Transactional(readOnly = true)
    public List<DepositResponse> getPendingDeposits() {
        return depositRepository.findByStatusOrderByCreatedAtDesc("PENDING")
                .stream()
                .map(depositMapper::toResponse)
                .collect(Collectors.toList());
    }
}
