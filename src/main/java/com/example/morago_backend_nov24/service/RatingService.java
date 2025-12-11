package com.example.morago_backend_nov24.service;

import com.example.morago_backend_nov24.dto.RatingRequest;
import com.example.morago_backend_nov24.dto.RatingResponse;
import com.example.morago_backend_nov24.entity.Call;
import com.example.morago_backend_nov24.entity.Rating;
import com.example.morago_backend_nov24.enums.CallStatus;
import com.example.morago_backend_nov24.repository.CallRepository;
import com.example.morago_backend_nov24.repository.RatingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class RatingService {

    private final CallRepository callRepository;
    private final RatingRepository ratingRepository;

    @Transactional
    public RatingResponse rateCall(Long callId, Long currentUserId, RatingRequest request) {

        Call call = callRepository.findById(callId)
                .orElseThrow(() -> new IllegalArgumentException("Call not found"));


        if (!call.getUserProfile().getId().equals(currentUserId)) {
            throw new IllegalArgumentException("You are not allowed to rate this call");
        }

        if (!CallStatus.COMPLETED.name().equals(call.getStatus())) {
            throw new IllegalArgumentException("Only completed calls can be rated");
        }


        if (call.getRating() != null || ratingRepository.findByCallId(callId).isPresent()) {
            throw new IllegalArgumentException("This call is already rated");
        }


        Rating rating = new Rating();
        rating.setCall(call);
        rating.setRatedByUser(call.getUserProfile());
        rating.setRatedTranslator(call.getTranslatorProfile());
        rating.setScore(request.getScore());
        rating.setCreatedAt(LocalDateTime.now());

        Rating saved = ratingRepository.save(rating);
        log.info("User {} rated call {} with score {}", currentUserId, callId, request.getScore());

        return RatingResponse.from(saved);
    }
}
