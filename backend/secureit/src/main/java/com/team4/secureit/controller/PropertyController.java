package com.team4.secureit.controller;

import com.team4.secureit.dto.request.CSRCreationRequest;
import com.team4.secureit.dto.response.PropertyDetailsResponse;
import com.team4.secureit.dto.response.PropertyResponse;
import com.team4.secureit.model.PropertyType;
import com.team4.secureit.service.PropertyService;
import jakarta.validation.Valid;
import org.bouncycastle.operator.OperatorCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/property")
public class PropertyController {
    @Autowired
    private PropertyService objectService;

    @PostMapping
    public void createObjext(@RequestBody @Valid final CSRCreationRequest csrCreationRequest) throws OperatorCreationException, IOException {
//        CSRDetails csrDetails = csrService.generateAndPersistCSR(csrCreationRequest, subscriber);
//        return new ResponseCreated("Successfully created object.", new UUID());
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public List<PropertyResponse> getProperties(@RequestParam(value = "search", required = false) String search, @RequestParam(value = "type", required = false) PropertyType type) {
        return objectService.getProperties(search, type);
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('PROPERTY_OWNER')")
    public List<PropertyResponse> getPropertiesForUser(@RequestParam(value = "id") UUID id, @RequestParam(value = "search", required = false) String search, @RequestParam(value = "type", required = false) PropertyType type) {
        return objectService.getPropertiesForUser(id, search, type);
    }

    @GetMapping()
    public PropertyDetailsResponse getProperty(@RequestParam(value = "id") UUID id) {
        return objectService.getPropertyById(id);
    }
    
    @GetMapping("/owner")
    public List<PropertyResponse> getPropertiesOfOwner(@RequestParam(value = "email") String email) {
        return objectService.getPropertiesOfOwner(email);
    }

    @GetMapping("/types")
    public List<String> getPropertyTypes() {
        return objectService.getPropertyTypes();
    }
}
