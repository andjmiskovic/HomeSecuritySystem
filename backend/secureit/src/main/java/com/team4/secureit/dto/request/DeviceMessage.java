package com.team4.secureit.dto.request;

import com.team4.secureit.validation.NoHTMLTags;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class DeviceMessage {
    @NotBlank
    @NoHTMLTags
    private String data;

    @NotNull
    private UUID deviceId;
}
