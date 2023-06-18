package com.team4.secureit.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class AlarmItem {
    private String deviceId;
    private String time;
    private String message;
}
