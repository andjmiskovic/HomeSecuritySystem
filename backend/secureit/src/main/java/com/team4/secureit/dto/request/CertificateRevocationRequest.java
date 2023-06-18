package com.team4.secureit.dto.request;

import com.team4.secureit.validation.NoHTMLTags;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CertificateRevocationRequest {
    @Size(max = 256)
    @NoHTMLTags
    private String reason;
}
