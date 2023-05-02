package com.team4.secureit.controller;

import com.team4.secureit.dto.request.CSRCreationRequest;
import com.team4.secureit.model.Property;
import com.team4.secureit.model.PropertyType;
import com.team4.secureit.service.PropertyService;
import jakarta.validation.Valid;
import org.bouncycastle.operator.OperatorCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

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
    public List<Property> getProperties(@RequestParam(value = "search", required = false) String search, @RequestParam(value = "type", required = false) PropertyType type) {
        List<Property> props = objectService.getProperties(search, type);
        return props;
    }

}
