package com.example.morago_backend_nov24.service;

import com.example.morago_backend_nov24.entity.Call;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CallNotificationService {

    private final SimpMessagingTemplate messagingTemplate;

    public void sendCallRequest(Call call) {
        Map<String, Object> notification = new HashMap<>();
        notification.put("type", "CALL_REQUEST");
        notification.put("callId", call.getId());
        notification.put("userId", call.getUserProfile().getId());
        notification.put("userName", call.getUserProfile().getName());
        notification.put("themeName", call.getTheme().getName());

        // Option 1: Send to specific user
        messagingTemplate.convertAndSendToUser(
                call.getTranslatorProfile().getId().toString(),
                "/queue/calls",
                notification
        );

        log.info("Sent call request to translator {}", call.getTranslatorProfile().getId());
    }

    public void sendCallAccepted(Call call) {
        Map<String, Object> notification = new HashMap<>();
        notification.put("type", "CALL_ACCEPTED");
        notification.put("callId", call.getId());
        notification.put("translatorId", call.getTranslatorProfile().getId());
        notification.put("translatorName", call.getTranslatorProfile().getFirstName() + " " +
                call.getTranslatorProfile().getLastName());

        messagingTemplate.convertAndSendToUser(
                call.getUserProfile().getId().toString(),
                "/queue/calls",
                notification
        );

        log.info("Sent call accepted to user {}", call.getUserProfile().getId());
    }

    public void sendCallRejected(Call call) {
        Map<String, Object> notification = new HashMap<>();
        notification.put("type", "CALL_REJECTED");
        notification.put("callId", call.getId());
        notification.put("translatorId", call.getTranslatorProfile().getId());

        messagingTemplate.convertAndSendToUser(
                call.getUserProfile().getId().toString(),
                "/queue/calls",
                notification
        );

        log.info("Sent call rejected to user {}", call.getUserProfile().getId());
    }

    public void sendCallEnded(Call call, String notifyParty) {
        Map<String, Object> notification = new HashMap<>();
        notification.put("type", "CALL_ENDED");
        notification.put("callId", call.getId());

        Long recipientId = "user".equals(notifyParty) ?
                call.getUserProfile().getId() :
                call.getTranslatorProfile().getId();

        messagingTemplate.convertAndSendToUser(
                recipientId.toString(),
                "/queue/calls",
                notification
        );

        log.info("Sent call ended to {}", notifyParty);
    }
}