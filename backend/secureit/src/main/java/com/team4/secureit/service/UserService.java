package com.team4.secureit.service;

import com.team4.secureit.dto.request.UserDetailsRequest;
import com.team4.secureit.dto.response.UserDetailResponse;
import com.team4.secureit.dto.response.UserInfoResponse;
import com.team4.secureit.exception.UserNotFoundException;
import com.team4.secureit.model.PropertyOwner;
import com.team4.secureit.repository.PropertyOwnerRepository;
import com.team4.secureit.util.MappingUtils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class UserService {

    @Autowired
    private PropertyOwnerRepository propertyOwnerRepository;


    public List<UserInfoResponse> getPropertyOwners() {
        return MappingUtils.toUserInfoResponseList(propertyOwnerRepository.findAll());
    }

    public UserDetailResponse getPropertyOwner(String userEmail) {
        return MappingUtils.toUserDetailsResponse(propertyOwnerRepository.findByEmail(userEmail).orElseThrow(UserNotFoundException::new));
    }

    public UserDetailResponse editPropertyOwner(UserDetailsRequest userRequest) {
        PropertyOwner propertyOwner = propertyOwnerRepository.findByEmail(userRequest.getEmail())
                .orElseThrow(UserNotFoundException::new);
        propertyOwner.setPhoneNumber(userRequest.getPhoneNumber());
        propertyOwner.setCity(userRequest.getCity());
        propertyOwner.setFirstName(userRequest.getFirstName());
        propertyOwner.setLastName(userRequest.getLastName());
        propertyOwnerRepository.save(propertyOwner);
        return MappingUtils.toUserDetailsResponse(propertyOwner);
    }
}
