package com.team4.secureit.controller;

import com.team4.secureit.api.ResponseOk;
import com.team4.secureit.dto.request.*;
import com.team4.secureit.dto.response.PropertyDetailsResponse;
import com.team4.secureit.dto.response.PropertyResponse;
import com.team4.secureit.model.PropertyType;
import com.team4.secureit.service.PropertyService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/property")
public class PropertyController {
    @Autowired
    private PropertyService propertyService;

    @PostMapping
    public ResponseOk create(@RequestBody @Valid final CreatePropertyRequest createPropertyRequest) {
        propertyService.createProperty(createPropertyRequest);
        return new ResponseOk("Success");
    }

    @PutMapping
    public ResponseOk update(@RequestBody @Valid final UpdatePropertyRequest updatePropertyRequest) {
        propertyService.updateProperty(updatePropertyRequest);
        return new ResponseOk("Success");
    }

    @DeleteMapping("/{propertyId}")
    public ResponseOk delete(@PathVariable UUID propertyId) {
        propertyService.deleteProperty(propertyId);
        return new ResponseOk("Success");
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public List<PropertyResponse> getProperties(@RequestParam(value = "search", required = false) String search, @RequestParam(value = "type", required = false) PropertyType type) {
        return propertyService.getProperties(search, type);
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('PROPERTY_OWNER')")
    public List<PropertyResponse> getPropertiesForUser(@RequestParam(value = "id") UUID id, @RequestParam(value = "search", required = false) String search, @RequestParam(value = "type", required = false) PropertyType type) {
        return propertyService.getPropertiesForUser(id, search, type);
    }

    @GetMapping()
    public PropertyDetailsResponse getProperty(@RequestParam(value = "id") UUID id) {
        return propertyService.getPropertyById(id);
    }

    @GetMapping("/owner")
    public List<PropertyResponse> getPropertiesOfOwner(Authentication authentication) {
        return propertyService.getPropertiesOfOwner(authentication);
    }

    @PostMapping("/set-owner")
    public void setOwner(@RequestBody TenantRoleRequest request) {
        propertyService.setOwner(request);
    }

    @PostMapping("/remove-tenant")
    public void removeTenant(@RequestBody TenantRoleRequest request) {
        propertyService.removeTenant(request);
    }

    @GetMapping("/types")
    public List<String> getPropertyTypes() {
        return propertyService.getPropertyTypes();
    }

    @PostMapping("/invite")
    public ResponseOk sendInvitationToProperty(@Valid @RequestBody InviteUserToPropertyRequest inviteUserToPropertyRequest) {
        propertyService.sendInvitationToProperty(inviteUserToPropertyRequest);
        return new ResponseOk("Success");
    }

    @PutMapping("/invite/verify")
    public ResponseOk verifyInviteToProperty(@Valid @RequestBody VerificationRequest verificationRequest) {
        propertyService.verifyInvite(verificationRequest);
        return new ResponseOk("Success");
    }
}
