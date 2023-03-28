package com.team4.secureit.dto.request;

import com.team4.secureit.validation.PasswordsMatch;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@PasswordsMatch
public class RegistrationRequest {
    @Email
    private String email;

    @NotBlank
    @Size(min = 12, message = "Password must be longer than 12 characters.")
    private String password;

    @NotBlank
    private String passwordConfirmation;

    @NotBlank
    @Size(max = 40, message = "First name cannot be longer than 40 characters.")
    private String firstName;

    @NotBlank
    @Size(max = 40, message = "Last name cannot be longer than 40 characters.")
    private String lastName;
}
