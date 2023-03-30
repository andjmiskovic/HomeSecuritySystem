package com.team4.secureit.controller;

import com.team4.secureit.model.CertificateDetails;
import com.team4.secureit.service.CertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/issuers")
    @PreAuthorize("hasRole('ADMIN')")
    public List<CertificateDetails> findIssuerCertificates() {
        return certificateService.findIssuerCertificates();
    }
}
