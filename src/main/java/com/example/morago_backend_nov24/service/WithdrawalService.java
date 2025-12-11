package com.example.morago_backend_nov24.service;

import com.example.morago_backend_nov24.dto.WithdrawalResponse;
import com.example.morago_backend_nov24.entity.TranslatorProfile;
import com.example.morago_backend_nov24.entity.Withdrawal;
import com.example.morago_backend_nov24.enums.WithdrawalStatus;
import com.example.morago_backend_nov24.mapper.WithdrawalMapper;
import com.example.morago_backend_nov24.repository.TranslatorProfileRepository;
import com.example.morago_backend_nov24.repository.WithdrawalRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class WithdrawalService {

    private final WithdrawalRepository withdrawalRepository;
    private final TranslatorProfileRepository translatorProfileRepository;
    private final WithdrawalMapper withdrawalMapper;

    @Transactional
    public Withdrawal requestWithdrawal(Long translatorId, Long amount,
                                        String bankName, String bankAccount) {
        if (amount == null || amount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than 0");
        }

        TranslatorProfile translator = translatorProfileRepository.findById(translatorId)
                .orElseThrow(() -> new IllegalArgumentException("Translator not found"));

        Long currentBalance = translator.getBalance() != null ? translator.getBalance() : 0L;
        if (currentBalance < amount) {
            throw new IllegalArgumentException("Insufficient balance. Current balance: " +
                    currentBalance + " KRW, Requested: " + amount + " KRW");
        }

        Withdrawal withdrawal = new Withdrawal();
        withdrawal.setTranslatorProfile(translator);
        withdrawal.setAmount(amount);
        withdrawal.setBankName(bankName);
        withdrawal.setBankAccount(bankAccount);
        withdrawal.setStatus(WithdrawalStatus.PENDING);
        withdrawal.setCreatedAt(LocalDateTime.now());

        Withdrawal saved = withdrawalRepository.save(withdrawal);
        log.info("Translator {} requested withdrawal of {} KRW", translatorId, amount);

        return saved;
    }

    @Transactional
    public void approveWithdrawal(Long withdrawalId) {
        Withdrawal withdrawal = withdrawalRepository.findById(withdrawalId)
                .orElseThrow(() -> new IllegalArgumentException("Withdrawal not found"));

        if (withdrawal.getStatus() != WithdrawalStatus.PENDING) {
            throw new IllegalArgumentException("Withdrawal is not pending");
        }

        TranslatorProfile translator = withdrawal.getTranslatorProfile();
        Long currentBalance = translator.getBalance() != null ? translator.getBalance() : 0L;
        if (currentBalance < withdrawal.getAmount()) {
            throw new IllegalArgumentException("Translator has insufficient balance");
        }

        translator.setBalance(currentBalance - withdrawal.getAmount());
        translatorProfileRepository.save(translator);

        withdrawal.setStatus(WithdrawalStatus.COMPLETED);
        withdrawal.setCompletedAt(LocalDateTime.now());
        withdrawalRepository.save(withdrawal);

        log.info("Withdrawal {} approved. Translator {} balance decreased by {} KRW. New balance: {} KRW",
                withdrawalId, translator.getId(), withdrawal.getAmount(), translator.getBalance());
    }

    @Transactional
    public void rejectWithdrawal(Long withdrawalId, String reason) {
        Withdrawal withdrawal = withdrawalRepository.findById(withdrawalId)
                .orElseThrow(() -> new IllegalArgumentException("Withdrawal not found"));

        if (withdrawal.getStatus() != WithdrawalStatus.PENDING) {
            throw new IllegalArgumentException("Withdrawal is not pending");
        }

        withdrawal.setStatus(WithdrawalStatus.REJECTED);
        withdrawal.setCompletedAt(LocalDateTime.now());
        withdrawalRepository.save(withdrawal);

        log.info("Withdrawal {} rejected. Reason: {}", withdrawalId, reason);
    }

    @Transactional(readOnly = true)
    public List<WithdrawalResponse> getAdminPendingWithdrawals() {
        return withdrawalRepository.findByStatusOrderByCreatedAtDesc(WithdrawalStatus.PENDING)
                .stream()
                .map(withdrawalMapper::toAdminResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<WithdrawalResponse> getAdminAllWithdrawals() {
        return withdrawalRepository.findAll()
                .stream()
                .map(withdrawalMapper::toAdminResponse)
                .toList();
    }

}
