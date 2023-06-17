package com.team4.secureit.dto.request;

import com.team4.secureit.validation.NoHTMLTags;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DeviceSensorInfo {
    @NotBlank
    @NoHTMLTags
    private String name;

    @NotBlank
    @NoHTMLTags
    private String unit;
}
