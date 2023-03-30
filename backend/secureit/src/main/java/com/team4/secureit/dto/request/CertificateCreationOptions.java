package com.team4.secureit.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

@Data
public class CertificateCreationOptions {

    @Valid
    private Extensions extensions;

    @NotNull
    @Positive
    private BigDecimal issuerSerialNumber;

    @Data
    public static class Extensions {
        @NotNull(message = "Extensions cannot be null")
        private Map<@NotBlank(message = "Extension key cannot be blank") String, @NotBlank(message = "Extension value cannot be blank") String> map;
    }
}
