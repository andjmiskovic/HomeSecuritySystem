package com.team4.secureit.dto.request;

import com.team4.secureit.validation.AlgorithmAndKeySize;
import com.team4.secureit.validation.CountryCode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
@AlgorithmAndKeySize
public class CSRCreationRequest {

    @NotBlank(message = "Common name cannot be blank")
    private String commonName;

    @NotBlank(message = "Organization name cannot be blank")
    private String organization;

    @NotBlank(message = "City cannot be blank")
    private String city;

    @NotBlank(message = "State cannot be blank")
    private String state;

    @NotBlank(message = "Country code cannot be blank")
    @CountryCode
    private String country;

    @NotBlank(message = "Algorithm cannot be blank")
    @Pattern(regexp = "^(RSA|DSA|EC)$", message = "Invalid algorithm. Allowed algorithms are RSA, DSA and EC.")
    private String algorithm;

    @NotNull
    @Positive(message = "Keysize must be a positive integer.")
    private int keySize;

}
