package com.team4.secureit.dto.request;

import com.team4.secureit.validation.NoHTMLTags;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CSRRejectionRequest {

    @NotBlank
    @NoHTMLTags
    private String rejectionReason;
}
