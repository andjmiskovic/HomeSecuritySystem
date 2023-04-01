package com.team4.secureit.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CertificateRevocationRequest {
    @Size(max = 256)
    private String reason;
}
