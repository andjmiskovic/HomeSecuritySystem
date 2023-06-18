package com.team4.secureit.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {
    @Email
    private String email;

    @NotBlank
    private String password;

    private String code;
}
