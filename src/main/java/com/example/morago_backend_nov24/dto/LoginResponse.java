package com.example.morago_backend_nov24.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class LoginResponse {
    private String accessToken;
    private Long userId;
    private String phone;
    private List<String> roles;
    private String activeRole;
}
