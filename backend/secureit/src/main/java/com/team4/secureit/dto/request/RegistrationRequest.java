package com.team4.secureit.dto.request;

import com.team4.secureit.validation.PasswordsMatch;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
@PasswordsMatch
public class RegistrationRequest extends UserDetailsRequest {
//    @Email
//    private String email;

    @NotBlank
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{12,}$",
            message = "Password must have at least 12 characters, at least one uppercase letter, " +
                    "one lowercase letter, one digit, and one special character.")
    private String password;

    @NotBlank
    private String passwordConfirmation;

//    @NotBlank
//    @Size(max = 40, message = "First name cannot be longer than 40 characters.")
//    private String firstName;
//
//    @NotBlank
//    @Size(max = 40, message = "Last name cannot be longer than 40 characters.")
//    private String lastName;
//
//    @NotBlank
//    @Pattern(regexp = "^[\\+]?[(]?[0-9]{3}[)]?[-\\s\\.]?[0-9]{3}[-\\s\\.]?[0-9]{4,6}$",
//            message = "Phone number is not in valid format.")
//    private String phoneNumber;
//
//    @NotBlank
//    @Size(max = 40, message = "City name cannot be longer than 40 characters.")
//    private String city;
}
