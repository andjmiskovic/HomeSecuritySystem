package com.team4.secureit.service;

import com.team4.secureit.dto.request.CreatePropertyRequest;
import com.team4.secureit.dto.request.TenantRoleRequest;
import com.team4.secureit.dto.request.UpdatePropertyRequest;
import com.team4.secureit.dto.response.PropertyDetailsResponse;
import com.team4.secureit.dto.response.PropertyResponse;
import com.team4.secureit.dto.response.UserInfoResponse;
import com.team4.secureit.exception.PropertyDoesNotBelongToUserException;
import com.team4.secureit.exception.PropertyNotFoundException;
import com.team4.secureit.exception.UserNotFoundException;
import com.team4.secureit.model.Property;
import com.team4.secureit.model.PropertyOwner;
import com.team4.secureit.model.PropertyType;
import com.team4.secureit.repository.PropertyOwnerRepository;
import com.team4.secureit.repository.PropertyRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

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

    public List<PropertyResponse> getPropertiesForUser(UUID id, String search, PropertyType type) {
        List<Property> properties = propertyRepository.getPropertiesWhereUserIsOwner(id, search, type);
        properties.addAll(propertyRepository.getPropertiesWhereUserIsTenant(id, search, type));
        return getPropertyDetailsResponsesFromProperties(properties);
    }

    public List<PropertyResponse> getPropertiesOfOwner(String email) {
        PropertyOwner propertyOwner = propertyOwnerRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
        List<Property> properties = propertyRepository.getPropertiesWhereUserIsOwner(propertyOwner.getId(), "", null);
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
        return new PropertyResponse(property.getId(), property.getName(), property.getAddress(), property.getType(), property.getImage(), new UserInfoResponse(owner.getId().toString(), owner.getFirstName(), owner.getLastName(), owner.getEmail()));
    }

    public List<String> getPropertyTypes() {
        return Arrays.stream(PropertyType.values())
                .map(Enum::name).toList();
    }

    public UserInfoResponse getUserInfo(UUID id) {
        PropertyOwner owner = propertyOwnerRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User for given id does not exist."));
        return new UserInfoResponse(owner.getId().toString(), owner.getFirstName(), owner.getLastName(), owner.getEmail());
    }

    public PropertyDetailsResponse getPropertyById(UUID id) {
        Property property = propertyRepository.findById(id).orElseThrow(PropertyNotFoundException::new);
        List<UserInfoResponse> tenants = new ArrayList<>();
        for (PropertyOwner tenant : property.getTenants()) {
            tenants.add(new UserInfoResponse(tenant.getId().toString(), tenant.getFirstName(), tenant.getLastName(), tenant.getEmail()));
        }
        return new PropertyDetailsResponse(property.getId(), property.getName(), property.getAddress(), property.getType(), property.getImage(), getUserInfo(property.getOwnerId()), tenants);
    }

    public void deleteAllPropertiesForUser(PropertyOwner propertyOwner) {
        for (Property property : propertyOwner.getOwnedProperties()) {
            property.setDeleted(true);
            propertyRepository.save(property);
        }
    }

    public void deleteProperty(UUID propertyId) {
        Property property = propertyRepository.findById(propertyId).orElseThrow(PropertyNotFoundException::new);
        property.setDeleted(true);
        propertyRepository.save(property);
    }

    public void createProperty(CreatePropertyRequest createPropertyRequest) {
        Property property = new Property();
        property.setDeleted(false);
        property.setAddress(createPropertyRequest.getAddress());
        property.setImage(createPropertyRequest.getImage());
        property.setName(createPropertyRequest.getName());
        property.setTenants(new HashSet<>());
        property.setType(createPropertyRequest.getType());
        property.setOwnerId(createPropertyRequest.getOwnerId());
        propertyRepository.save(property);
    }

    public void updateProperty(UpdatePropertyRequest updatePropertyRequest) {
        PropertyOwner owner = propertyOwnerRepository.findById(updatePropertyRequest.getOwnerId()).orElseThrow(UserNotFoundException::new);
        Property property = propertyRepository.findById(updatePropertyRequest.getPropertyId()).orElseThrow(PropertyNotFoundException::new);
        if (!owner.getOwnedProperties().contains(property)) {
            throw new PropertyDoesNotBelongToUserException();
        } else if (!property.getOwnerId().equals(owner.getId())) {
            throw new PropertyDoesNotBelongToUserException();
        }
        property.setType(updatePropertyRequest.getType());
        property.setName(updatePropertyRequest.getName());
        property.setAddress(updatePropertyRequest.getAddress());
        property.setImage(updatePropertyRequest.getImage());
        propertyRepository.save(property);
    }

    public void setOwner(TenantRoleRequest request) {
        Property property = propertyRepository.findById(request.getPropertyId()).orElseThrow(PropertyNotFoundException::new);

        PropertyOwner propertyOwner = propertyOwnerRepository.findById(property.getOwnerId()).get();
        propertyOwner.getOwnedProperties().remove(property);
        propertyOwner.getTenantProperties().add(property);

        PropertyOwner newOwner = propertyOwnerRepository.findById(request.getTenantId()).get();
        newOwner.getOwnedProperties().add(property);
        newOwner.getTenantProperties().remove(property);

        Set<PropertyOwner> tenants = property.getTenants();
        tenants.remove(newOwner);
        tenants.add(propertyOwner);
        property.setTenants(tenants);
        property.setOwnerId(request.getTenantId());

        propertyOwnerRepository.save(propertyOwner);
        propertyOwnerRepository.save(newOwner);
        propertyRepository.save(property);
    }

    public void removeTenant(TenantRoleRequest request) {
        Property property = propertyRepository.findById(request.getPropertyId()).orElseThrow(PropertyNotFoundException::new);

        PropertyOwner propertyOwner = propertyOwnerRepository.findById(request.getTenantId()).get();
        propertyOwner.getTenantProperties().remove(property);

        property.getTenants().remove(propertyOwner);
        propertyOwnerRepository.save(propertyOwner);
        propertyRepository.save(property);
    }
}
