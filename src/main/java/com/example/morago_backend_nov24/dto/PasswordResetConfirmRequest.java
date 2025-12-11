// src/main/java/com/example/morago_backend_nov24/dto/PasswordResetConfirmRequest.java
package com.example.morago_backend_nov24.dto;

import com.example.morago_backend_nov24.enums.RoleType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordResetConfirmRequest {

    @NotBlank
    private String phone;

    @NotNull
    private RoleType role; // USER or TRANSLATOR

    @NotBlank
    private String verificationCode;

    @NotBlank
    private String newPassword;

    @NotBlank
    private String confirmPassword;
}
