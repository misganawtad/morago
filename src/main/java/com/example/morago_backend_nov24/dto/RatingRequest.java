package com.example.morago_backend_nov24.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RatingRequest {

    @NotNull
    @Min(1)
    @Max(5)
    private Integer score;
}

