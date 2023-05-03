package com.team4.secureit.service;

import com.team4.secureit.dto.response.PropertyDetailsResponse;
import com.team4.secureit.dto.response.PropertyResponse;
import com.team4.secureit.dto.response.UserInfoResponse;
import com.team4.secureit.exception.UserNotFoundException;
import com.team4.secureit.model.Property;
import com.team4.secureit.model.PropertyOwner;
import com.team4.secureit.model.PropertyType;
import com.team4.secureit.repository.PropertyOwnerRepository;
import com.team4.secureit.repository.PropertyRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class PropertyService {

    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private PropertyOwnerRepository propertyOwnerRepository;

    public List<PropertyResponse> getProperties(String search, PropertyType type) {
        List<Property> properties = propertyRepository.getAll(search.toLowerCase(), type);
        return getPropertyDetailsResponsesFromProperties(properties);
    }

    public List<PropertyResponse> getOwnerProperties(UUID id, String search, PropertyType type) {
        List<Property> properties = propertyRepository.getPropertiesWhereUserIsOwner(id, search, type);
        properties.addAll(propertyRepository.getPropertiesWhereUserIsTenant(id, search, type));
        return getPropertyDetailsResponsesFromProperties(properties);
    }

    private List<PropertyResponse> getPropertyDetailsResponsesFromProperties(List<Property> properties) {
        List<PropertyResponse> response = new ArrayList<>();
        for (Property property : properties) {
            response.add(getPropertyDetailsResponseFromProperty(property));
        }
        return response;
    }


    private PropertyResponse getPropertyDetailsResponseFromProperty(Property property) {
        PropertyOwner owner = propertyOwnerRepository.findById(property.getOwnerId()).orElseThrow(() -> new UserNotFoundException("User for given id does not exist."));
        return new PropertyResponse(property.getId(), property.getName(), property.getAddress(), property.getType(), property.getImage(), new UserInfoResponse(owner.getId(), owner.getFirstName(), owner.getLastName(), owner.getEmail()));
    }

    public List<String> getPropertyTypes() {
        return Arrays.stream(PropertyType.values())
                .map(Enum::name).toList();
    }

    public UserInfoResponse getUserInfo(UUID id) {
        PropertyOwner owner = propertyOwnerRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User for given id does not exist."));
        return new UserInfoResponse(owner.getId(), owner.getFirstName(), owner.getLastName(), owner.getEmail());
    }

    public PropertyDetailsResponse getPropertyById(String id) {
        Property property = propertyRepository.getReferenceById(UUID.fromString(id));
        List<UserInfoResponse> tenants = new ArrayList<>();
        return new PropertyDetailsResponse(property.getId(), property.getName(), property.getAddress(), property.getType(), property.getImage(), getUserInfo(property.getOwnerId()), tenants);
    }
}
