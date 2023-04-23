package com.team4.secureit.util;

import com.team4.secureit.dto.response.UserDetailResponse;
import com.team4.secureit.dto.response.UserInfoResponse;
import com.team4.secureit.model.PropertyOwner;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

public class MappingUtils {
    private static final ModelMapper modelMapper = new ModelMapper();

    public static UserInfoResponse toUserInfoResponse(PropertyOwner propertyOwner) {
        return modelMapper.map(propertyOwner, UserInfoResponse.class);
    }

    public static List<UserInfoResponse> toUserInfoResponseList(List<PropertyOwner> users) {
        return users.stream()
                .map(MappingUtils::toUserInfoResponse)
                .collect(Collectors.toList());
    }

    public static UserDetailResponse toUserDetailsResponse(PropertyOwner propertyOwner) {
        return modelMapper.map(propertyOwner, UserDetailResponse.class);
    }
}
