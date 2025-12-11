package com.example.morago_backend_nov24.mapper;

import com.example.morago_backend_nov24.dto.CallResponse;
import com.example.morago_backend_nov24.entity.Call;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CallMapper {
    CallResponse toResponse(Call call);
}
