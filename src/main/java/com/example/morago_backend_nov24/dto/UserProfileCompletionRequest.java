package com.example.morago_backend_nov24.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class UserProfileCompletionRequest {

    private String name;          // display name for USER
    private LocalDate birthDate;  // optional, stored in base User.birthDate
}
