package com.team4.secureit.controller;

import com.team4.secureit.api.ResponseOk;
import com.team4.secureit.dto.request.CertificateRevocationRequest;
import com.team4.secureit.dto.request.LoginVerificationRequest;
import com.team4.secureit.dto.response.CertificateValidityResponse;
import com.team4.secureit.model.CertificateDetails;
import com.team4.secureit.model.CertificateRevocation;
import com.team4.secureit.model.Role;
import com.team4.secureit.model.User;
import com.team4.secureit.service.CertificateService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigInteger;
import java.security.KeyStoreException;
import java.util.List;

@RestController
@RequestMapping(path = "/certificates")
public class CertificateController {

    @Autowired
    private CertificateService certificateService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'PROPERTY_OWNER')")
    public List<CertificateDetails> getAll(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        if (user.getRole().equals(Role.ROLE_ADMIN))
            return certificateService.getAll();
        else
            return certificateService.findAllBySubscriber(user);
    }

    @GetMapping("/{serialNumber}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROPERTY_OWNER')")
    public CertificateDetails getBySerialNumber(@PathVariable final BigInteger serialNumber) throws EntityNotFoundException {
        return certificateService.getBySerialNumber(serialNumber);
    }

    @GetMapping("/{serialNumber}/validity")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROPERTY_OWNER')")
    public CertificateValidityResponse checkCertificateValidity(@PathVariable final BigInteger serialNumber) throws EntityNotFoundException, KeyStoreException {
        return certificateService.checkValidityForSerialNumber(serialNumber);
    }

    @PostMapping("/{serialNumber}/revoke")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseOk revokeCertificate(@PathVariable final BigInteger serialNumber, @RequestBody CertificateRevocationRequest request) {
        certificateService.revokeCertificate(serialNumber, request);
        return new ResponseOk("Certificate revoked successfully.");
    }

    @PostMapping("/{serialNumber}/privateKey")
    @PreAuthorize("hasRole('PROPERTY_OWNER')")
    public ResponseEntity<String> getPrivateKey(@PathVariable final BigInteger serialNumber, @RequestBody @Valid LoginVerificationRequest verificationRequest, Authentication authentication) throws IOException {
        User subscriber = (User) authentication.getPrincipal();
        String pem = certificateService.getPrivateKey(serialNumber, verificationRequest, subscriber);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + subscriber.getEmail() + "_priv.pem\"")
                .contentLength(pem.length())
                .contentType(MediaType.TEXT_PLAIN)
                .body(pem);
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
