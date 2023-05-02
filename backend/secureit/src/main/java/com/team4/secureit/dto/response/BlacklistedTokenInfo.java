package com.team4.secureit.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.Instant;

@Data
public class BlacklistedTokenInfo {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String subject;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String issuedAt;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String expiresAt;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String error;

    private Instant added = Instant.now();

    public BlacklistedTokenInfo(String subject, String issuedAt, String expiresAt) {
        this.subject = subject;
        this.issuedAt = issuedAt;
        this.expiresAt = expiresAt;
    }

    public BlacklistedTokenInfo(String error) {
        this.error = error;
    }
}
