package com.example.morago_backend_nov24.service;

import com.example.morago_backend_nov24.dto.*;
import com.example.morago_backend_nov24.entity.Call;
import com.example.morago_backend_nov24.entity.TranslatorProfile;
import com.example.morago_backend_nov24.entity.Withdrawal;
import com.example.morago_backend_nov24.enums.CallStatus;
import com.example.morago_backend_nov24.enums.WithdrawalStatus;
import com.example.morago_backend_nov24.mapper.CallMapper;
import com.example.morago_backend_nov24.mapper.WithdrawalMapper;
import com.example.morago_backend_nov24.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TranslatorProfileService {

    private final TranslatorProfileRepository translatorProfileRepository;
    private final CallRepository callRepository;
    private final WithdrawalRepository withdrawalRepository;
    private final WithdrawalService withdrawalService;
    private final TopicThemeRepository topicThemeRepository;
    private final LanguageRepository languageRepository;

    // 👇 NEW: mappers
    private final CallMapper callMapper;
    private final WithdrawalMapper withdrawalMapper;

    @Transactional
    public void completeProfile(Long translatorId, TranslatorProfileCompletionRequest request) {
        TranslatorProfile translator = translatorProfileRepository.findById(translatorId)
                .orElseThrow(() -> new IllegalStateException("Translator profile not found"));

        translator.setFirstName(request.getFirstName());
        translator.setLastName(request.getLastName());
        translator.setBirthDate(request.getBirthDate());
        translator.setTopikLevel(request.getTopikLevel());

        if (request.getThemeIds() != null && !request.getThemeIds().isEmpty()) {
            var themes = new java.util.HashSet<>(topicThemeRepository.findAllById(request.getThemeIds()));
            translator.setThemes(themes);
        }

        if (request.getLanguageIds() != null && !request.getLanguageIds().isEmpty()) {
            var languages = new java.util.HashSet<>(languageRepository.findAllById(request.getLanguageIds()));
            translator.setLanguages(languages);
        }

        translatorProfileRepository.save(translator);
        log.info("Translator {} profile completed", translatorId);
    }

    @Transactional(readOnly = true)
    public TranslatorDashboardResponse getDashboard(Long translatorId) {
        TranslatorProfile translator = translatorProfileRepository.findById(translatorId)
                .orElseThrow(() -> new IllegalArgumentException("Translator not found"));

        List<Call> completedCalls = callRepository.findByTranslatorProfileIdAndStatus(
                translatorId, CallStatus.COMPLETED);

        Long totalEarnings = completedCalls.stream()
                .mapToLong(call -> call.getSum() != null ? call.getSum() : 0L)
                .sum();

        List<Withdrawal> pendingWithdrawals = withdrawalRepository
                .findByTranslatorProfileIdAndStatus(translatorId, WithdrawalStatus.PENDING);

        return TranslatorDashboardResponse.builder()
                .balance(translator.getBalance())
                .totalEarnings(totalEarnings)
                .totalCalls(completedCalls.size())
                .pendingWithdrawals(pendingWithdrawals.size())
                .build();
    }

    @Transactional(readOnly = true)
    public TranslatorBalanceResponse getBalance(Long translatorId) {
        TranslatorProfile translator = translatorProfileRepository.findById(translatorId)
                .orElseThrow(() -> new IllegalArgumentException("Translator not found"));

        return TranslatorBalanceResponse.builder()
                .balance(translator.getBalance())
                .translatorId(translator.getId())
                .translatorName(translator.getFirstName() + " " + translator.getLastName())
                .build();
    }

    @Transactional(readOnly = true)
    public List<CallResponse> getCalls(Long translatorId, String status) {
        List<Call> calls;
        if (status != null) {
            CallStatus callStatus = CallStatus.valueOf(status);
            calls = callRepository.findByTranslatorProfileIdAndStatus(translatorId, callStatus);
        } else {
            calls = callRepository.findByTranslatorProfileIdOrderByStartedAtDesc(translatorId);
        }

        return calls.stream()
                // .map(CallResponse::from)
                .map(callMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TranslatorEarningsResponse getEarnings(Long translatorId) {
        List<Call> completedCalls = callRepository.findByTranslatorProfileIdAndStatus(
                translatorId, CallStatus.COMPLETED);

        Long totalEarnings = completedCalls.stream()
                .mapToLong(call -> call.getSum() != null ? call.getSum() : 0L)
                .sum();

        Long todayEarnings = completedCalls.stream()
                .filter(call -> call.getEndedAt() != null &&
                        call.getEndedAt().toLocalDate().equals(java.time.LocalDate.now()))
                .mapToLong(call -> call.getSum() != null ? call.getSum() : 0L)
                .sum();

        return TranslatorEarningsResponse.builder()
                .totalEarnings(totalEarnings)
                .todayEarnings(todayEarnings)
                .totalCalls(completedCalls.size())
                .averagePerCall(completedCalls.isEmpty() ? 0 : totalEarnings / completedCalls.size())
                .build();
    }

    @Transactional
    public WithdrawalResponse requestWithdrawal(Long translatorId, WithdrawalRequest request) {
        Withdrawal withdrawal = withdrawalService.requestWithdrawal(
                translatorId,
                request.getAmount(),
                request.getBankName(),
                request.getBankAccount()
        );
        // return WithdrawalResponse.from(withdrawal);
        return withdrawalMapper.toResponse(withdrawal);
    }

    @Transactional(readOnly = true)
    public List<WithdrawalResponse> getWithdrawals(Long translatorId) {
        return withdrawalRepository.findByTranslatorProfileIdOrderByCreatedAtDesc(translatorId)
                .stream()
                // .map(WithdrawalResponse::from)
                .map(withdrawalMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<WithdrawalResponse> getPendingWithdrawals(Long translatorId) {
        return withdrawalRepository.findByTranslatorProfileIdAndStatus(
                        translatorId, WithdrawalStatus.PENDING)
                .stream()
                // .map(WithdrawalResponse::from)
                .map(withdrawalMapper::toResponse)
                .collect(Collectors.toList());
    }
}
