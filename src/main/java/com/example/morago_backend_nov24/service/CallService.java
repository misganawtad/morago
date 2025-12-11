package com.example.morago_backend_nov24.service;

import com.example.morago_backend_nov24.dto.CallResponse;
import com.example.morago_backend_nov24.entity.*;
import com.example.morago_backend_nov24.enums.CallStatus;
import com.example.morago_backend_nov24.mapper.CallMapper;
import com.example.morago_backend_nov24.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CallService {

    private final CallRepository callRepository;
    private final UserProfileRepository userProfileRepository;
    private final TranslatorProfileRepository translatorProfileRepository;
    private final TopicThemeRepository topicThemeRepository;
    private final CallNotificationService callNotificationService;
    private final CallMapper callMapper;


    private static final Long MIN_BALANCE = 100L;
    private static final int COST_PER_MINUTE = 100; // 100 KRW per minute

    @Transactional
    public Long requestCall(Long userId, Long translatorId, Long themeId) {
        UserProfile user = userProfileRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        if (user.getBalance() == null || user.getBalance() < MIN_BALANCE) {
            throw new IllegalArgumentException(
                    "Insufficient balance. Current: " +
                            (user.getBalance() != null ? user.getBalance() : 0) +
                            " KRW, Required: at least " + MIN_BALANCE + " KRW"
            );
        }
        TranslatorProfile translator = translatorProfileRepository.findById(translatorId)
                .orElseThrow(() -> new IllegalArgumentException("Translator not found"));
        TopicTheme theme = topicThemeRepository.findById(themeId)
                .orElseThrow(() -> new IllegalArgumentException("Theme not found"));
        Call call = new Call();
        call.setUserProfile(user);
        call.setTranslatorProfile(translator);
        call.setTheme(theme);
        call.setStatus(CallStatus.PENDING.name());
        call.setStartedAt(LocalDateTime.now());

        Call saved = callRepository.save(call);
        callNotificationService.sendCallRequest(saved);

        log.info("Call {} requested by user {} for translator {}",
                saved.getId(), userId, translatorId);

        return saved.getId();
    }

    @Transactional
    public void acceptCall(Long callId, Long translatorId) {
        Call call = callRepository.findById(callId)
                .orElseThrow(() -> new IllegalArgumentException("Call not found"));

        if (!call.getTranslatorProfile().getId().equals(translatorId)) {
            throw new IllegalArgumentException("Not authorized to accept this call");
        }

        if (!CallStatus.PENDING.name().equals(call.getStatus())) {
            throw new IllegalArgumentException(
                    "Call is not pending. Current status: " + call.getStatus()
            );
        }

        call.setStatus(CallStatus.IN_PROGRESS.name());
        call.setStartedAt(LocalDateTime.now());
        callRepository.save(call);

        callNotificationService.sendCallAccepted(call);

        log.info("Call {} accepted by translator {}", callId, translatorId);
    }

    @Transactional
    public void rejectCall(Long callId, Long translatorId) {
        Call call = callRepository.findById(callId)
                .orElseThrow(() -> new IllegalArgumentException("Call not found"));

        if (!call.getTranslatorProfile().getId().equals(translatorId)) {
            throw new IllegalArgumentException("Not authorized to reject this call");
        }

        if (!CallStatus.PENDING.name().equals(call.getStatus())) {
            throw new IllegalArgumentException("Call is not pending");
        }

        call.setStatus(CallStatus.REJECTED.name());
        call.setEndedAt(LocalDateTime.now());
        callRepository.save(call);

        callNotificationService.sendCallRejected(call);

        log.info("Call {} rejected by translator {}", callId, translatorId);
    }

    @Transactional
    public void endCall(Long callId, Long userId) {
        Call call = callRepository.findById(callId)
                .orElseThrow(() -> new IllegalArgumentException("Call not found"));

        boolean isUser = call.getUserProfile().getId().equals(userId);
        boolean isTranslator = call.getTranslatorProfile().getId().equals(userId);

        if (!isUser && !isTranslator) {
            throw new IllegalArgumentException("Not authorized to end this call");
        }

        if (!CallStatus.IN_PROGRESS.name().equals(call.getStatus())) {
            throw new IllegalArgumentException("Call is not in progress");
        }
        call.setEndedAt(LocalDateTime.now());
        if (call.getStartedAt() != null) {
            Duration duration = Duration.between(call.getStartedAt(), call.getEndedAt());
            call.setDurationSeconds((int) duration.getSeconds());

            int minutes = (int) Math.ceil(call.getDurationSeconds() / 60.0);
            Long cost = (long) (minutes * COST_PER_MINUTE);
            call.setSum(cost);
            transferCallPayment(call.getUserProfile(), call.getTranslatorProfile(), cost);
        }

        call.setStatus(CallStatus.COMPLETED.name());
        callRepository.save(call);

        callNotificationService.sendCallEnded(call, isUser ? "translator" : "user");

        log.info("Call {} ended. Duration: {}s, Cost: {} KRW",
                callId, call.getDurationSeconds(), call.getSum());
    }

    private void transferCallPayment(UserProfile user, TranslatorProfile translator, Long amount) {
        Long userBalance = user.getBalance() != null ? user.getBalance() : 0L;
        if (userBalance < amount) {
            throw new IllegalArgumentException("Insufficient balance to complete call");
        }
        user.setBalance(userBalance - amount);
        userProfileRepository.save(user);
        Long translatorBalance = translator.getBalance() != null ? translator.getBalance() : 0L;
        translator.setBalance(translatorBalance + amount);
        translatorProfileRepository.save(translator);

        log.info("Payment transferred: User {} paid {} KRW to Translator {}",
                user.getId(), amount, translator.getId());
    }

    @Transactional(readOnly = true)
    public List<CallResponse> getUserCalls(Long userId) {
        return callRepository.findByUserProfileIdOrderByStartedAtDesc(userId)
                .stream()
                .map(callMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CallResponse> getTranslatorCalls(Long translatorId) {
        return callRepository.findByTranslatorProfileIdOrderByStartedAtDesc(translatorId)
                .stream()
                .map(callMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CallResponse> getTranslatorPendingCalls(Long translatorId) {
        return callRepository.findByTranslatorProfileIdAndStatus(
                        translatorId, CallStatus.PENDING)
                .stream()
                .map(callMapper::toResponse)
                .collect(Collectors.toList());
    }
}