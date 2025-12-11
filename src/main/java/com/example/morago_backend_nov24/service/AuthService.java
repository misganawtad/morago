package com.example.morago_backend_nov24.service;

import com.example.morago_backend_nov24.dto.*;
import com.example.morago_backend_nov24.entity.*;
import com.example.morago_backend_nov24.enums.RoleType;
import com.example.morago_backend_nov24.enums.VerificationType;
import com.example.morago_backend_nov24.repository.*;
import com.example.morago_backend_nov24.security.CustomUserDetails;
import com.example.morago_backend_nov24.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final TranslatorProfileRepository translatorProfileRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final PhoneVerificationService phoneVerificationService;
    private final PhoneVerificationRepository phoneVerificationRepository;
    private final SmsService smsService;
    private final MessagingService messagingService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Transactional
    public SignupResponse signup(SignupRequest request) {
        phoneVerificationService.validateSignupCode(
                request.getPhone(),
                request.getVerificationCode()
        );
        if (userRepository.existsByPhone(request.getPhone())) {
            throw new IllegalArgumentException("Phone is already in use");
        }

        return switch (request.getRole()) {
            case USER -> signupAsUser(request);
            case TRANSLATOR -> signupAsTranslator(request);
            default -> throw new IllegalArgumentException("Invalid role");
        };
    }


    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {
        // Authenticate user
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getPhone(),
                        request.getPassword()
                )
        );

        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
        String requestedRole = request.getProfileType().name(); // "USER" or "TRANSLATOR"
        boolean hasRequestedRole = principal.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_" + requestedRole));

        if (!hasRequestedRole) {
            throw new IllegalArgumentException(
                    "User does not have " + requestedRole + " role. " +
                            "Available roles: " + principal.getAuthorities().stream()
                            .map(a -> a.getAuthority().replace("ROLE_", ""))
                            .collect(Collectors.joining(", "))
            );
        }


        String token = jwtUtil.generateToken(
                principal.getId(),
                principal.getPhone(),
                requestedRole
        );


        return new LoginResponse(
                token,
                principal.getId(),
                principal.getPhone(),
                principal.getAuthorities().stream()
                        .map(a -> a.getAuthority())
                        .collect(Collectors.toList()),
                requestedRole
        );
    }

    private SignupResponse signupAsUser(SignupRequest request) {
        Role userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new IllegalStateException("Role USER not found"));

        UserProfile profile = new UserProfile();
        profile.setPhone(request.getPhone());
        profile.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        profile.setStatus("ACTIVE");
        profile.setCreatedAt(LocalDateTime.now());
        profile.setRoles(Set.of(userRole));
        profile.setBalance(0L);

        UserProfile saved = userProfileRepository.save(profile);
        return new SignupResponse(saved.getId(), saved.getPhone(), "USER");
    }

    private SignupResponse signupAsTranslator(SignupRequest request) {
        Role translatorRole = roleRepository.findByName("TRANSLATOR")
                .orElseThrow(() -> new IllegalStateException("Role TRANSLATOR not found"));
        Role userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new IllegalStateException("Role USER not found"));

        TranslatorProfile translator = new TranslatorProfile();
        translator.setPhone(request.getPhone());
        translator.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        translator.setStatus("ACTIVE");
        translator.setCreatedAt(LocalDateTime.now());
        translator.setRoles(Set.of(userRole, translatorRole));
        translator.setBalance(0L);
        TranslatorProfile saved = translatorProfileRepository.save(translator);
        return new SignupResponse(saved.getId(), saved.getPhone(), "TRANSLATOR");
    }

    private User findUserByPhoneAndRoleOrThrow(String phone, RoleType role) {
        User user = userRepository.findByPhone(phone)
                .orElseThrow(() -> new IllegalArgumentException("User not found with this phone"));

        boolean hasRole = user.getRoles().stream()
                .anyMatch(r -> r.getName().equals(role.name()));

        if (!hasRole) {
            throw new IllegalArgumentException("User does not have role " + role.name());
        }

        return user;
    }

    @Transactional
    public void sendPasswordResetCode(PasswordResetCodeRequest request) {
        User user = findUserByPhoneAndRoleOrThrow(request.getPhone(), request.getRole());

        String code = String.valueOf((int)(Math.random() * 900_000) + 100_000);

        PhoneVerification verification = PhoneVerification.builder()
                .phone(user.getPhone())
                .code(code)
                .type(VerificationType.PASSWORD_RESET)
                .expiresAt(LocalDateTime.now().plusMinutes(10))
                .used(false)
                .build();

        phoneVerificationRepository.save(verification);
        messagingService.sendPasswordResetCode(user.getPhone(), code);
    }

    @Transactional
    public void resetPassword(PasswordResetConfirmRequest request) {
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new IllegalArgumentException("Passwords do not match");
        }

        PhoneVerification verification = phoneVerificationRepository
                .findTopByPhoneAndCodeAndTypeAndUsedFalseOrderByIdDesc(
                        request.getPhone(),
                        request.getVerificationCode(),
                        VerificationType.PASSWORD_RESET
                )
                .orElseThrow(() -> new IllegalArgumentException("Invalid or already used code"));

        if (verification.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Code has expired");
        }

        User user = findUserByPhoneAndRoleOrThrow(request.getPhone(), request.getRole());

        user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        verification.setUsed(true);
        phoneVerificationRepository.save(verification);
    }
}