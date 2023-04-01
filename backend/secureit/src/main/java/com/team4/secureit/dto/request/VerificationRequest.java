package com.team4.secureit.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class VerificationRequest {
    @NotNull(message = "Code is not provided")
    @Pattern(regexp = "^[0-9a-fA-F]{64}$", message = "Invalid format")
    private String code;
}
