package com.team4.secureit.dto.response;

import com.team4.secureit.model.PropertyOwner;
import com.team4.secureit.model.PropertyType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class PropertyDetailsResponse {
    private UUID id;
    private String name;
    private String address;
    private PropertyType type;
    private String image;
    private UserInfoResponse owner;
}
