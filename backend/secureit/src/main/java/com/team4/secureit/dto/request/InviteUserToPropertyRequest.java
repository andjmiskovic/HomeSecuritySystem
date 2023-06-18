package com.team4.secureit.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.UUID;

@Data
public class InviteUserToPropertyRequest {
    private UUID propertyId;
    @Email
    private String userEmail;
}
