package com.team4.secureit.controller;

import com.team4.secureit.api.ResponseCreated;
import com.team4.secureit.api.ResponseOk;
import com.team4.secureit.dto.request.CSRCreationRequest;
import com.team4.secureit.dto.request.CSRRejectionRequest;
import com.team4.secureit.dto.request.CertificateCreationOptions;
import com.team4.secureit.model.CSRDetails;
import com.team4.secureit.model.Role;
import com.team4.secureit.model.User;
import com.team4.secureit.service.CSRService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.bouncycastle.operator.OperatorCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
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
    public ResponseCreated createCSR(@RequestBody @Valid final CSRCreationRequest csrCreationRequest, Authentication authentication) throws OperatorCreationException, IOException {
        User subscriber = (User) authentication.getPrincipal();
        CSRDetails csrDetails = csrService.generateAndPersistCSR(csrCreationRequest, subscriber);
        return new ResponseCreated("Successfully created CSR.", csrDetails.getId());
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'PROPERTY_OWNER')")
    public List<CSRDetails> getAll(@RequestParam(value = "status", required = false) String status, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        if (user.getRole().equals(Role.ROLE_PROPERTY_OWNER)) {
            if (status != null) {
                return csrService.findByStatusAndBySubscriber(status, user);
            } else {
                return csrService.getAllBySubscriber(user);
            }
        } else if (user.getRole().equals(Role.ROLE_ADMIN)) {
            if (status != null) {
                return csrService.findByStatus(status);
            } else {
                return csrService.getAll();
            }
        }
        return null;
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROPERTY_OWNER')")
    public CSRDetails getById(@PathVariable final UUID id) throws EntityNotFoundException {
        return csrService.getById(id);
    }

    @PostMapping("/{id}/certificate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseOk issueCertificate(@PathVariable final UUID id, @RequestBody @Valid final CertificateCreationOptions options) throws Exception {
        csrService.issueAndPersistCertificate(id, options);
        return new ResponseOk("CSR approved successfully.");
    }

    @PostMapping("/{id}/reject")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseOk rejectRequest(@PathVariable final UUID id, @RequestBody @Valid final CSRRejectionRequest request) throws EntityNotFoundException {
        csrService.rejectRequest(id, request.getRejectionReason());
        return new ResponseOk("CSR rejected successfully.");
    }
}
