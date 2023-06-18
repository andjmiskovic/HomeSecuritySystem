package com.team4.secureit.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DeviceChangeAlarmsRequest {
    @NotNull
    private String[][] alarms;
}
