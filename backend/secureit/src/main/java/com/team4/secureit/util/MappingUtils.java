package com.team4.secureit.util;

import com.team4.secureit.dto.response.DeviceDetailsResponse;
import com.team4.secureit.dto.response.PropertyResponse;
import com.team4.secureit.dto.response.UserDetailResponse;
import com.team4.secureit.dto.response.UserInfoResponse;
import com.team4.secureit.model.Device;
import com.team4.secureit.model.Property;
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

    public static DeviceDetailsResponse toDeviceDetailsResponse(Device device) {
        Property property = device.getProperty();
        PropertyOwner owner = property.getOwner();

        UserInfoResponse ownerInfo = new UserInfoResponse(
                owner.getId().toString(),
                owner.getFirstName(),
                owner.getLastName(),
                owner.getEmail()
        );

        PropertyResponse propertyInfo = new PropertyResponse(
                property.getId(),
                property.getName(),
                property.getAddress(),
                property.getType(),
                property.getImage(),
                ownerInfo
        );

        return new DeviceDetailsResponse(
                device.getId(),
                device.getName(),
                device.getType(),
                device.getManufacturer(),
                device.getMacAddress(),
                device.getLabel(),
                propertyInfo,
                device.getSensorInfo()
        );
    }
}
