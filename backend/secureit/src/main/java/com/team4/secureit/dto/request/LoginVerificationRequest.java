package com.team4.secureit.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginVerificationRequest {

    @NotBlank
    private String password;
}
