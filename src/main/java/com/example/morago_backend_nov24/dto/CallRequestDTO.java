package com.example.morago_backend_nov24.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class CallRequestDTO {
    @NotNull(message = "Translator ID is required")
    @Positive(message = "Translator ID must be positive")
    private Long translatorId;

    @NotNull(message = "Theme ID is required")
    @Positive(message = "Theme ID must be positive")
    private Long themeId;
}
