package com.team4.secureit.service;

import com.team4.secureit.dto.request.*;
import com.team4.secureit.dto.response.PropertyDetailsResponse;
import com.team4.secureit.dto.response.PropertyResponse;
import com.team4.secureit.dto.response.UserInfoResponse;
import com.team4.secureit.exception.PropertyDoesNotBelongToUserException;
import com.team4.secureit.exception.PropertyNotFoundException;
import com.team4.secureit.exception.UserNotFoundException;
import com.team4.secureit.exception.VerificationFailedException;
import com.team4.secureit.model.*;
import com.team4.secureit.repository.PropertyOwnerRepository;
import com.team4.secureit.repository.PropertyRepository;
import com.team4.secureit.repository.TenantInviteRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.team4.secureit.util.LoginUtils.generateVerificationCode;

@Service
@Transactional
public class PropertyService {

    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private PropertyOwnerRepository propertyOwnerRepository;

    @Autowired
    private TenantInviteRepository tenantInviteRepository;

    @Autowired
    private MailingService mailingService;

    public List<PropertyResponse> getProperties(String search, PropertyType type) {
        List<Property> properties = propertyRepository.getAll(search.toLowerCase(), type);
        return getPropertyDetailsResponsesFromProperties(properties);
    }

    public List<PropertyResponse> getPropertiesForUser(UUID id, String search, PropertyType type) {
        List<Property> properties = propertyRepository.getPropertiesWhereUserIsOwner(id, search, type);
        properties.addAll(propertyRepository.getPropertiesWhereUserIsTenant(id, search, type));
        return getPropertyDetailsResponsesFromProperties(properties);
    }

    public List<PropertyResponse> getPropertiesOfOwner(Authentication authentication) {
        PropertyOwner propertyOwner = (PropertyOwner) authentication.getPrincipal();
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
        PropertyOwner owner = propertyOwnerRepository.findById(property.getOwner().getId()).orElseThrow(() -> new UserNotFoundException("User for given id does not exist."));
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
            if (!tenant.isDeleted()) {
                tenants.add(new UserInfoResponse(tenant.getId().toString(), tenant.getFirstName(), tenant.getLastName(), tenant.getEmail()));
            }
        }
        return new PropertyDetailsResponse(property.getId(), property.getName(), property.getAddress(), property.getType(), property.getImage(), getUserInfo(property.getOwner().getId()), tenants);
    }

    public void deleteAllPropertiesForUser(PropertyOwner propertyOwner) {
        for (Property property : propertyRepository.getPropertiesWhereUserIsOwner(propertyOwner.getId(), "", null)) {
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
        property.setTenants(new ArrayList<>());
        property.setType(createPropertyRequest.getType());
        property.setOwner(propertyOwnerRepository.findById(createPropertyRequest.getOwnerId()).get());
        propertyRepository.save(property);
    }

    public void updateProperty(UpdatePropertyRequest updatePropertyRequest) {
        PropertyOwner owner = propertyOwnerRepository.findById(updatePropertyRequest.getOwnerId()).orElseThrow(UserNotFoundException::new);
        Property property = propertyRepository.findById(updatePropertyRequest.getPropertyId()).orElseThrow(PropertyNotFoundException::new);
        if (!propertyRepository.getPropertiesWhereUserIsOwner(owner.getId(), "", null).contains(property)) {
            throw new PropertyDoesNotBelongToUserException();
        } else if (!property.getOwner().getId().equals(owner.getId())) {
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

        PropertyOwner propertyOwner = propertyOwnerRepository.findById(property.getOwner().getId()).get();

        PropertyOwner newOwner = propertyOwnerRepository.findById(request.getTenantId()).get();

        List<PropertyOwner> tenants = property.getTenants();
        tenants.removeIf(obj -> obj.getId() == newOwner.getId());
        tenants.add(propertyOwner);
        property.setTenants(tenants);
        property.setOwner(propertyOwnerRepository.findById(request.getTenantId()).get());

        propertyRepository.save(property);
    }

    public void removeTenant(TenantRoleRequest request) {
        Property property = propertyRepository.findById(request.getPropertyId()).orElseThrow(PropertyNotFoundException::new);

        PropertyOwner propertyOwner = propertyOwnerRepository.findById(request.getTenantId()).get();

        property.getTenants().removeIf(obj -> obj.getId() == propertyOwner.getId());

        propertyRepository.save(property);
    }

    public void sendInvitationToProperty(InviteUserToPropertyRequest inviteUserToPropertyRequest) {
        Property property = propertyRepository.findById(inviteUserToPropertyRequest.getPropertyId()).orElseThrow(PropertyNotFoundException::new);
        PropertyOwner propertyOwner = propertyOwnerRepository.findByEmail(inviteUserToPropertyRequest.getUserEmail()).orElseThrow(UserNotFoundException::new);

        TenantInvite tenantInvite = new TenantInvite();
        tenantInvite.setProperty(property);
        tenantInvite.setUser(propertyOwner);
        tenantInvite.setVerificationCode(generateVerificationCode());
        tenantInvite.setVerified(false);

        mailingService.sendInvitationToProperty(tenantInvite);

        tenantInviteRepository.save(tenantInvite);
    }

    public void verifyInvite(VerificationRequest verificationRequest) {
        Optional<TenantInvite> tenantInviteOptional = tenantInviteRepository.findByVerificationCode(verificationRequest.getCode());
        if (tenantInviteOptional.isPresent()) {
            TenantInvite tenantInvite = tenantInviteOptional.get();
            if (tenantInvite.isVerified()) {
                throw new VerificationFailedException("Request is already accepted.");
            }
            tenantInvite.setVerified(true);
            Property property = tenantInvite.getProperty();
            List<PropertyOwner> tenants = property.getTenants();
            tenants.add(tenantInvite.getUser());
            property.setTenants(tenants);

            tenantInviteRepository.save(tenantInvite);
            propertyRepository.save(property);
        } else {
            throw new VerificationFailedException("Error while verifying request");
        }
    }
}
