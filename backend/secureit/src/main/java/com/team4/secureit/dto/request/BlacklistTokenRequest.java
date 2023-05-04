package com.team4.secureit.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class BlacklistTokenRequest {
    @NotBlank
    private String token;
}
