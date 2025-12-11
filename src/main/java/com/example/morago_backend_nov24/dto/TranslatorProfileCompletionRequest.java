package com.example.morago_backend_nov24.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
public class TranslatorProfileCompletionRequest {

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @Past(message = "Birth date must be in the past")
    private LocalDate birthDate;

    private String topikLevel;
    private String topikCertificateUrl;
    private Set<Long> themeIds;
    private Set<Long> languageIds;
}
