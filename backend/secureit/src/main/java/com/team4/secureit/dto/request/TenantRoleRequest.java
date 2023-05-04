package com.team4.secureit.dto.request;

import lombok.Data;

import java.util.UUID;

@Data
public class TenantRoleRequest {
    private UUID propertyId;
    private UUID tenantId;
}
