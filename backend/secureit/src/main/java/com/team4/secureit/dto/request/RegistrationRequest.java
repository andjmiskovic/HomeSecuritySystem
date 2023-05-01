package com.team4.secureit.dto.request;

import com.team4.secureit.validation.NotCommon;
import com.team4.secureit.validation.PasswordsMatch;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
@PasswordsMatch
public class RegistrationRequest extends UserDetailsRequest {
    @NotBlank
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{12,}$",
            message = "Password must have at least 12 characters, at least one uppercase letter, " +
                    "one lowercase letter, one digit, and one special character.")
    @NotCommon
    private String password;

    @NotBlank
    private String passwordConfirmation;
}
