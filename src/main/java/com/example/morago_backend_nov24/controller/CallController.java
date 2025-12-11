package com.example.morago_backend_nov24.controller;

import com.example.morago_backend_nov24.dto.CallRequestDTO;
import com.example.morago_backend_nov24.dto.CallResponse;
import com.example.morago_backend_nov24.dto.RatingRequest;
import com.example.morago_backend_nov24.dto.RatingResponse;
import com.example.morago_backend_nov24.security.CustomUserDetails;
import com.example.morago_backend_nov24.service.CallService;
import com.example.morago_backend_nov24.service.RatingService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/calls")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class CallController {

    private final CallService callService;
    private final RatingService ratingService;

    @PostMapping("/request")
    public Long requestCall(@AuthenticationPrincipal CustomUserDetails userDetails,
                            @Valid @RequestBody CallRequestDTO request) {
        return callService.requestCall(
                userDetails.getId(),
                request.getTranslatorId(),
                request.getThemeId()
        );
    }

    @PostMapping("/{callId}/accept")
    public void acceptCall(@PathVariable Long callId,
                           @AuthenticationPrincipal CustomUserDetails userDetails) {
        callService.acceptCall(callId, userDetails.getId());
    }

    @PostMapping("/{callId}/reject")
    public void rejectCall(@PathVariable Long callId,
                           @AuthenticationPrincipal CustomUserDetails userDetails) {
        callService.rejectCall(callId, userDetails.getId());
    }

    @PostMapping("/{callId}/end")
    public void endCall(@PathVariable Long callId,
                        @AuthenticationPrincipal CustomUserDetails userDetails) {
        callService.endCall(callId, userDetails.getId());
    }

    @GetMapping("/my-calls")
    public List<CallResponse> getMyCalls(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return callService.getUserCalls(userDetails.getId());
    }

    @GetMapping("/translator/calls")
    public List<CallResponse> getTranslatorCalls(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return callService.getTranslatorCalls(userDetails.getId());
    }

    @GetMapping("/translator/pending")
    public List<CallResponse> getPendingCalls(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return callService.getTranslatorPendingCalls(userDetails.getId());
    }

    @PostMapping("/{callId}/rating")
    public RatingResponse rateCall(@PathVariable Long callId,
                                   @AuthenticationPrincipal CustomUserDetails userDetails,
                                   @RequestBody @Valid RatingRequest request) {
        return ratingService.rateCall(callId, userDetails.getId(), request);
    }
}