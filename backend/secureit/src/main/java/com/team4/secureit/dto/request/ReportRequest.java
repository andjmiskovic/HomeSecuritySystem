package com.team4.secureit.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
public class ReportRequest {
    private Date startDate;
    private Date endDate;
    @NotBlank
    private UUID deviceId;
}
