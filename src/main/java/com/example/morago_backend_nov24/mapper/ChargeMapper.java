package com.example.morago_backend_nov24.mapper;

import com.example.morago_backend_nov24.dto.ChargeResponse;
import com.example.morago_backend_nov24.entity.Charge;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ChargeMapper {

    ChargeResponse toResponse(Charge charge);
}