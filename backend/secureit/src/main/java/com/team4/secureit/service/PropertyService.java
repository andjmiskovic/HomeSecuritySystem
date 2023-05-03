package com.team4.secureit.service;

import com.team4.secureit.dto.response.PropertyDetailsResponse;
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

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class PropertyService {

    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private PropertyOwnerRepository propertyOwnerRepository;

    public List<PropertyDetailsResponse> getProperties(String search, PropertyType type) {
        List<Property> properties = propertyRepository.getAll(search.toLowerCase(), type);
        return getPropertyDetailsResponsesFromProperties(properties);
    }

    public List<PropertyDetailsResponse> getOwnerProperties(UUID id, String search, PropertyType type) {
        List<Property> properties = propertyRepository.getAllForOwner(id, search, type);
        return getPropertyDetailsResponsesFromProperties(properties);
    }

    private List<PropertyDetailsResponse> getPropertyDetailsResponsesFromProperties(List<Property> properties) {
        List<PropertyDetailsResponse> response = new ArrayList<>();
        for (Property property : properties) {
            response.add(getPropertyDetailsResponseFromProperty(property));
        }
        return response;
    }

    private PropertyDetailsResponse getPropertyDetailsResponseFromProperty(Property property) {
        PropertyOwner owner = propertyOwnerRepository.findById(property.getOwnerId()).orElseThrow(() -> new UserNotFoundException("User for given id does not exist."));
        return new PropertyDetailsResponse(property.getId(), property.getName(), property.getAddress(), property.getType(), property.getImage(), new UserInfoResponse(owner.getId(), owner.getFirstName(), owner.getLastName(), owner.getEmail()));
    }

    public List<String> getPropertyTypes() {
        return Arrays.stream(PropertyType.values())
                .map(Enum::name).toList();
    }


}
