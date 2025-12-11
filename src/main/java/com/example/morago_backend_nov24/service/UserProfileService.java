package com.example.morago_backend_nov24.service;

import com.example.morago_backend_nov24.dto.UserProfileCompletionRequest;
import com.example.morago_backend_nov24.entity.UserProfile;
import com.example.morago_backend_nov24.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserProfileService {

    private final UserProfileRepository userProfileRepository;

    @Transactional
    public void completeProfile(Long currentUserId, UserProfileCompletionRequest request) {
        UserProfile profile = userProfileRepository.findById(currentUserId)
                .orElseThrow(() -> new IllegalStateException("User profile not found"));

        profile.setName(request.getName());


        userProfileRepository.save(profile);
    }
}
