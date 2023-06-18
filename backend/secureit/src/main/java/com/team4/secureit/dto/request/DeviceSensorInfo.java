package com.team4.secureit.dto.request;

import com.team4.secureit.validation.NoHTMLTags;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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

    @Pattern(regexp = "^(int|long|float|string|double|boolean)$", message = "Invalid data type. Allowed types: int,long,float,string,double,boolean")
    private String type;
}
