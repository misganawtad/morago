package com.example.morago_backend_nov24.controller;

import com.example.morago_backend_nov24.dto.UserProfileCompletionRequest;
import com.example.morago_backend_nov24.security.CustomUserDetails;
import com.example.morago_backend_nov24.service.UserProfileService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class UserProfileController {

    private final UserProfileService userProfileService;

    @PutMapping("/me/profile")
    public void completeProfile(@Valid @RequestBody UserProfileCompletionRequest request,
                                @AuthenticationPrincipal CustomUserDetails userDetails) {
        userProfileService.completeProfile(userDetails.getId(), request);
    }
}