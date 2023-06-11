package com.team4.secureit.dto.request;

import lombok.Data;
import lombok.NonNull;

import java.util.UUID;

@Data
public class DevicePairingInitRequest {
    @NonNull
    private UUID propertyId;
}
