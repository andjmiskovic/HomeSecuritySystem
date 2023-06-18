package com.team4.secureit.dto.response;

import lombok.Data;

@Data
public class AlarmItem {
    private String deviceId;
    private String time;
    private String message;
}
