package com.team4.secureit.dto.request;

import com.team4.secureit.model.PropertyType;
import com.team4.secureit.validation.NoHTMLTags;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.UUID;

@Data
public class CreatePropertyRequest {

    private UUID ownerId;
    @NotBlank(message = "Property name cannot be blank")
    @NoHTMLTags
    private String name;
    @NotBlank(message = "Property address cannot be blank")
    @NoHTMLTags
    private String address;
    private PropertyType type;
    @NoHTMLTags
    private String image;
}
