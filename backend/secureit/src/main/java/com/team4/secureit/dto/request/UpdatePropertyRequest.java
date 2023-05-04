package com.team4.secureit.dto.request;

import lombok.Data;

import java.util.UUID;

@Data
public class UpdatePropertyRequest extends CreatePropertyRequest{
    private UUID propertyId;
}
