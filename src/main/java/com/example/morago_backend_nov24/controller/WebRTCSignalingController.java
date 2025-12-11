package com.example.morago_backend_nov24.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
public class WebRTCSignalingController {

    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/webrtc/{callId}/offer")
    public void handleOffer(@DestinationVariable Long callId,
                            @Payload Map<String, Object> payload) {

        Long senderId = ((Number) payload.get("senderId")).longValue();
        Long recipientId = ((Number) payload.get("recipientId")).longValue();
        String sdp = (String) payload.get("sdp");

        log.info("Received WebRTC offer for call {} from user {} to translator {}",
                callId, senderId, recipientId);
        Map<String, Object> message = Map.of(
                "type", "offer",
                "callId", callId,
                "senderId", senderId,
                "sdp", sdp
        );

        messagingTemplate.convertAndSendToUser(
                recipientId.toString(),
                "/queue/webrtc",
                message
        );

        log.info("Forwarded offer to user {}", recipientId);
    }


    @MessageMapping("/webrtc/{callId}/answer")
    public void handleAnswer(@DestinationVariable Long callId,
                             @Payload Map<String, Object> payload) {

        Long senderId = ((Number) payload.get("senderId")).longValue();
        Long recipientId = ((Number) payload.get("recipientId")).longValue();
        String sdp = (String) payload.get("sdp");

        log.info("Received WebRTC answer for call {} from translator {} to user {}",
                callId, senderId, recipientId);

        // Forward answer to recipient
        Map<String, Object> message = Map.of(
                "type", "answer",
                "callId", callId,
                "senderId", senderId,
                "sdp", sdp
        );

        messagingTemplate.convertAndSendToUser(
                recipientId.toString(),
                "/queue/webrtc",
                message
        );

        log.info("Forwarded answer to user {}", recipientId);
    }

    @MessageMapping("/webrtc/{callId}/ice-candidate")
    public void handleIceCandidate(@DestinationVariable Long callId,
                                   @Payload Map<String, Object> payload) {

        Long senderId = ((Number) payload.get("senderId")).longValue();
        Long recipientId = ((Number) payload.get("recipientId")).longValue();
        Map<String, Object> candidate = (Map<String, Object>) payload.get("candidate");

        log.info("Received ICE candidate for call {} from {} to {}",
                callId, senderId, recipientId);
        Map<String, Object> message = Map.of(
                "type", "ice-candidate",
                "callId", callId,
                "senderId", senderId,
                "candidate", candidate
        );

        messagingTemplate.convertAndSendToUser(
                recipientId.toString(),
                "/queue/webrtc",
                message
        );

        log.info("Forwarded ICE candidate to user {}", recipientId);
    }
    @MessageMapping("/webrtc/{callId}/ready")
    public void handleParticipantReady(@DestinationVariable Long callId,
                                       @Payload Map<String, Object> payload) {

        Long senderId = ((Number) payload.get("senderId")).longValue();
        Long recipientId = ((Number) payload.get("recipientId")).longValue();

        log.info("Participant {} is ready for call {}", senderId, callId);
        Map<String, Object> message = Map.of(
                "type", "participant-ready",
                "callId", callId,
                "participantId", senderId
        );
        messagingTemplate.convertAndSendToUser(
                recipientId.toString(),
                "/queue/webrtc",
                message
        );
    }
}