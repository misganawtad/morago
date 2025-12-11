package com.example.morago_backend_nov24.mapper;

import com.example.morago_backend_nov24.dto.WithdrawalResponse;
import com.example.morago_backend_nov24.entity.Withdrawal;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WithdrawalMapper {

    WithdrawalResponse toResponse(Withdrawal withdrawal);
    WithdrawalResponse toAdminResponse(Withdrawal withdrawal);
}
