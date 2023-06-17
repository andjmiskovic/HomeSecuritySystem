package com.team4.secureit.dto.request;

import lombok.Data;

import java.util.Map;
import java.util.UUID;

@Data
public class DeviceMessage {
    private Map<String, String> measures;

    private String timestamp;

    private UUID deviceId;
}
