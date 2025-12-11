// src/main/java/com/example/morago_backend_nov24/dto/PasswordResetCodeRequest.java
package com.example.morago_backend_nov24.dto;

import com.example.morago_backend_nov24.enums.RoleType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordResetCodeRequest {

    @NotBlank
    private String phone;

    @NotNull
    private RoleType role;   // USER or TRANSLATOR
}
