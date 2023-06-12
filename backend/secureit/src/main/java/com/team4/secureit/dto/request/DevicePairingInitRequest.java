package com.team4.secureit.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class DevicePairingInitRequest {

    @NotNull
    private UUID propertyId;
}
