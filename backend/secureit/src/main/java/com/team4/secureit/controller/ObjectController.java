package com.team4.secureit.controller;

import com.team4.secureit.api.ResponseCreated;
import com.team4.secureit.dto.request.CSRCreationRequest;
import com.team4.secureit.model.CSRDetails;
import com.team4.secureit.model.User;
import com.team4.secureit.service.ObjectService;
import jakarta.validation.Valid;
import org.bouncycastle.operator.OperatorCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping(path = "/object")
public class ObjectController {
    @Autowired
    private ObjectService objectService;

    @PostMapping
    public void createObjext(@RequestBody @Valid final CSRCreationRequest csrCreationRequest) throws OperatorCreationException, IOException {
//        CSRDetails csrDetails = csrService.generateAndPersistCSR(csrCreationRequest, subscriber);
//        return new ResponseCreated("Successfully created object.", new UUID());
    }
}
