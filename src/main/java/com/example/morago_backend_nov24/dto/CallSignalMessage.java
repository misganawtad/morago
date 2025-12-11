package com.example.morago_backend_nov24.dto;

import com.example.morago_backend_nov24.enums.CallSignalType;
import lombok.Data;

@Data
public class CallSignalMessage {
    private CallSignalType type;
    private Long callId;
    private Long fromUserId;
    private Long toUserId;   // could be translator or user
    private Long themeId;    // optional, from your Call.theme
}
