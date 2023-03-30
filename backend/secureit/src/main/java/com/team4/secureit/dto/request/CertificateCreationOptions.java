package com.team4.secureit.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Map;

@Data
public class CertificateCreationOptions {

    @NotNull(message = "Extensions cannot be null")
    private Map<@NotBlank(message = "Extension key cannot be blank") String, @NotBlank(message = "Extension value cannot be blank") String> extensions;

    @NotNull
    private String issuerAlias;
}
