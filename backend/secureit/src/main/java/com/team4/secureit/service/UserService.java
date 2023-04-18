package com.team4.secureit.service;

import com.team4.secureit.dto.response.UserInfoResponse;
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
}
