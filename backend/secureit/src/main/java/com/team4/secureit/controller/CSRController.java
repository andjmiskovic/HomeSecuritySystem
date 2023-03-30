package com.team4.secureit.controller;

import com.team4.secureit.api.ResponseCreated;
import com.team4.secureit.api.ResponseOk;
import com.team4.secureit.dto.request.CSRCreationRequest;
import com.team4.secureit.dto.request.CSRRejectionRequest;
import com.team4.secureit.dto.request.CertificateCreationOptions;
import com.team4.secureit.model.PersistedCSR;
import com.team4.secureit.service.CSRService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.bouncycastle.operator.OperatorCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/csrs")
public class CSRController {

    @Autowired
    private CSRService csrService;

    @PostMapping
    @PreAuthorize("hasRole('PROPERTY_OWNER')")
    public ResponseCreated create(@RequestBody @Valid final CSRCreationRequest csrCreationRequest) throws OperatorCreationException, IOException {
        PersistedCSR persistedCSR = csrService.generateAndPersistCSR(csrCreationRequest);
        return new ResponseCreated("Successfully created CSR.", persistedCSR.getId());
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<PersistedCSR> getAll(@RequestParam(value = "status", required = false) String status) {
        if (status != null) {
            return csrService.findByStatus(status);
        } else {
            return csrService.getAll();
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public PersistedCSR getById(@PathVariable final UUID id) throws EntityNotFoundException {
        return csrService.getById(id);
    }

    @PostMapping("/{id}/certificate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseOk issueCertificate(@PathVariable final UUID id, @RequestBody final CertificateCreationOptions options) throws EntityNotFoundException, IOException {
        csrService.approve(id, options);
        return new ResponseOk("CSR approved successfully.");
    }

    @PutMapping("/{id}/reject")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseOk reject(@PathVariable final UUID id, @RequestBody final CSRRejectionRequest request) throws EntityNotFoundException {
        csrService.reject(id, request.getRejectionReason());
        return new ResponseOk("CSR rejected successfully.");
    }
}
