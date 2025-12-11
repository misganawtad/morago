package com.example.morago_backend_nov24.mapper;

import com.example.morago_backend_nov24.dto.DepositResponse;
import com.example.morago_backend_nov24.entity.Deposit;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DepositMapper {

    DepositResponse toResponse(Deposit deposit);
}
