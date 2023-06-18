package com.team4.secureit.dto.request;

import com.team4.secureit.validation.NoHTMLTags;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.util.List;

@Data
public class DeviceHandshakeData {
    @NotBlank
    @NoHTMLTags
    private String name;

    @NotBlank
    @NoHTMLTags
    private String label;

    @NotBlank
    @NoHTMLTags
    private String type;

    @NotBlank
    @NoHTMLTags
    private String manufacturer;

    @NotBlank
    @Pattern(regexp = "^([0-9A-Fa-f]{2}[:-]){5}([0-9A-Fa-f]{2})$", message = "Mac address is in invalid format.")
    private String macAddress;

    @NotBlank
    private String publicKey;

    @NotNull
    private List<@Valid DeviceSensorInfo> sensors;

    @NotNull
    private String[][] alarms;

}
