package com.team4.secureit.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class SetPasswordRequest {
    @NotBlank
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{12,}$",
            message = "Password must have at least 12 characters, at least one uppercase letter, " +
                    "one lowercase letter, one digit, and one special character.")
    private String password;

    @NotBlank
    private String passwordConfirmation;

    @NotNull(message = "Code is not provided")
    @Pattern(regexp = "^[0-9a-fA-F]{64}$", message = "Invalid format")
    private String verificationCode;
}
