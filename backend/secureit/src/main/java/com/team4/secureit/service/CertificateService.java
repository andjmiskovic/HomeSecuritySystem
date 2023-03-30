package com.team4.secureit.service;

import com.team4.secureit.dto.request.CertificateCreationOptions;
import com.team4.secureit.model.CertificateDetails;
import com.team4.secureit.repository.CertificateDetailsRepository;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.List;

@Service
public class CertificateService {

    @Autowired
    private CertificateDetailsRepository certificateDetailsRepository;

    public List<CertificateDetails> getAll() {
        return certificateDetailsRepository.findAll();
    }

    public List<CertificateDetails> findIssuerCertificates() {
        return certificateDetailsRepository.findIssuerCertificates();
    }

    public X509Certificate generateCertificate(PKCS10CertificationRequest csr, CertificateCreationOptions options, KeyPair issuerKeyPair) throws IOException {

        X500Name issuerX500name = new X500Name("ok");

        BigInteger serialNumber = new BigInteger(128, new SecureRandom());
        Date issuedAt = new Date();
        Date expiresAt = new Date(System.currentTimeMillis() + 365L * 24L * 60L * 60L * 1000L);

        X509v3CertificateBuilder certGen = new X509v3CertificateBuilder(
                issuerX500name,
                serialNumber,
                issuedAt,
                expiresAt,
                csr.getSubject(),
                csr.getSubjectPublicKeyInfo());

        return null;
    }
}
