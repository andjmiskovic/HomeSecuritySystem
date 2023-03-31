package com.team4.secureit.controller;

import com.team4.secureit.api.ResponseOk;
import com.team4.secureit.dto.request.CertificateRevocationRequest;
import com.team4.secureit.dto.response.CertificateValidityResponse;
import com.team4.secureit.model.CertificateDetails;
import com.team4.secureit.model.CertificateRevocation;
import com.team4.secureit.service.CertificateService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.security.KeyStoreException;
import java.util.List;

@RestController
@RequestMapping(path = "/certificates")
public class CertificateController {

    @Autowired
    private CertificateService certificateService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<CertificateDetails> getAll() {
        return certificateService.getAll();
    }

    @GetMapping("/{serialNumber}")
    @PreAuthorize("hasRole('ADMIN')")
    public CertificateDetails getBySerialNumber(@PathVariable final BigInteger serialNumber) throws EntityNotFoundException {
        return certificateService.getBySerialNumber(serialNumber);
    }

    @GetMapping("/{serialNumber}/validity")
    @PreAuthorize("hasRole('ADMIN')")
    public CertificateValidityResponse checkCertificateValidity(@PathVariable final BigInteger serialNumber) throws EntityNotFoundException, KeyStoreException {
        return certificateService.checkValidityForSerialNumber(serialNumber);
    }

    @PutMapping("/{serialNumber}/revoke")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseOk revokeCertificate(@PathVariable final BigInteger serialNumber, CertificateRevocationRequest request) {
        certificateService.revokeCertificate(serialNumber, request);
        return new ResponseOk("Certificate revoked successfully.");
    }

    @GetMapping("/issuers")
    @PreAuthorize("hasRole('ADMIN')")
    public List<CertificateDetails> findIssuerCertificates() {
        return certificateService.findIssuerCertificates();
    }

    @GetMapping("/revocations")
    @PreAuthorize("hasRole('ADMIN')")
    public List<CertificateRevocation> getAllRevocations() {
        return certificateService.getAllRevocations();
    }
}
