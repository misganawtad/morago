package com.example.morago_backend_nov24.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupCodeRequest {

    @NotBlank
    private String phone;
}
