package com.example.morago_backend_nov24.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SignupResponse {
    private Long userId;
    private String phone;
    private String profileType; // "USER" or "TRANSLATOR"
}
