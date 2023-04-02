package com.team4.secureit.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import com.team4.secureit.model.Role;

@Data
@AllArgsConstructor
public class LoginResponse {
    private String accessToken;
    private Long expiresAt;
    private Role role;
}
