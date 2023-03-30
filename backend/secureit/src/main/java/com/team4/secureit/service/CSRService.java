package com.team4.secureit.service;

import com.team4.secureit.dto.request.CSRCreationRequest;
import com.team4.secureit.dto.request.CertificateCreationOptions;
import com.team4.secureit.model.PersistedCSR;
import com.team4.secureit.model.RequestStatus;
import com.team4.secureit.repository.CertificateDetailsRepository;
import com.team4.secureit.repository.PersistedCSRRepository;
import com.team4.secureit.util.CertificateUtils;
import jakarta.persistence.EntityNotFoundException;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.pkcs.jcajce.JcaPKCS10CertificationRequestBuilder;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
public class CSRService {

    @Autowired
    private CertificateService certificateService;

    @Autowired
    private KeyStoreService keyStoreService;

    @Autowired
    private PersistedCSRRepository persistedCSRRepository;

    @Autowired
    private CertificateDetailsRepository certificateDetailsRepository;

    @SuppressWarnings("ConstantConditions")
    public PersistedCSR generateAndPersistCSR(CSRCreationRequest request) throws OperatorCreationException, IOException {
        KeyPair keyPair = generateKeyPair(request.getAlgorithm(), request.getKeySize()); // TODO: Store private key somewhere/somehow
        PKCS10CertificationRequest csr = generateCSR(request, keyPair);
        PersistedCSR persistedCSR = convertToPersistedCSR(request, csr);
        return persistedCSRRepository.save(persistedCSR);
    }

    public List<PersistedCSR> getAll() {
        return persistedCSRRepository.findAll();
    }

    public PersistedCSR getById(UUID id) throws EntityNotFoundException {
        return persistedCSRRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("CSR not found."));
    }

    public List<PersistedCSR> findByStatus(String status) {
        try {
            return persistedCSRRepository.findByStatus(RequestStatus.valueOf(status.toUpperCase()));
        } catch (IllegalArgumentException e) {
            return Collections.emptyList();
        }
    }

    public void issueCertificate(UUID id, CertificateCreationOptions options) throws IOException, CertificateException, KeyStoreException, OperatorCreationException {
        PersistedCSR persistedCSR = getById(id);
        PKCS10CertificationRequest csr = convertToPKCS10CR(persistedCSR);
        String alias = persistedCSR.getAlias();

        persistedCSR.setStatus(RequestStatus.APPROVED);
        persistedCSR.setProcessed(Instant.now());
        persistedCSRRepository.save(persistedCSR);

        X509Certificate cert = certificateService.generateCertificate(csr, options);
        keyStoreService.storeCertificate(cert, alias);
        certificateDetailsRepository.save(CertificateUtils.convertToDetails(cert, alias));
    }

    public void rejectRequest(UUID id, String reason) {
        PersistedCSR request = getById(id);

        request.setStatus(RequestStatus.REJECTED);
        request.setRejectionReason(reason);
        request.setProcessed(Instant.now());

        persistedCSRRepository.save(request);
    }

    private PKCS10CertificationRequest generateCSR(CSRCreationRequest request, KeyPair keyPair) throws OperatorCreationException {
        Security.addProvider(new BouncyCastleProvider());

        X500Name x500Name = new X500NameBuilder()
                .addRDN(BCStyle.CN, request.getCommonName())
                .addRDN(BCStyle.O, request.getOrganization())
                .addRDN(BCStyle.L, request.getCity())
                .addRDN(BCStyle.ST, request.getState())
                .addRDN(BCStyle.C, request.getCountry())
                .build();

        JcaPKCS10CertificationRequestBuilder csrBuilder = new JcaPKCS10CertificationRequestBuilder(x500Name, keyPair.getPublic());
        JcaContentSignerBuilder signerBuilder = new JcaContentSignerBuilder("SHA256WithRSAEncryption");
        ContentSigner contentSigner = signerBuilder.build(keyPair.getPrivate());
        return csrBuilder.build(contentSigner);
    }

    private KeyPair generateKeyPair(String algorithm, int keySize) {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(algorithm);
            keyPairGenerator.initialize(keySize);
            return keyPairGenerator.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    private PersistedCSR convertToPersistedCSR(CSRCreationRequest request, PKCS10CertificationRequest csr) throws IOException {
        PersistedCSR persistedCSR = new PersistedCSR();
        persistedCSR.setPem(convertToPEM(csr));
        persistedCSR.setAlias(request.getCommonName());
        persistedCSR.setCommonName(request.getCommonName());
        persistedCSR.setOrganization(request.getOrganization());
        persistedCSR.setCity(request.getCity());
        persistedCSR.setState(request.getState());
        persistedCSR.setCountry(request.getCountry());
        persistedCSR.setAlgorithm(request.getAlgorithm());
        persistedCSR.setKeySize(request.getKeySize());
        return persistedCSR;
    }

    private PKCS10CertificationRequest convertToPKCS10CR(PersistedCSR persistedCSR) throws IOException {
        return parsePEM(persistedCSR.getPem());
    }

    private String convertToPEM(PKCS10CertificationRequest csr) throws IOException {
        PemObject pemObject = new PemObject("CERTIFICATE REQUEST", csr.getEncoded());
        StringWriter stringWriter = new StringWriter();
        try (PemWriter pemWriter = new PemWriter(stringWriter)) {
            pemWriter.writeObject(pemObject);
        }
        return stringWriter.toString();
    }

    private PKCS10CertificationRequest parsePEM(String pem) throws IOException {
        try (PEMParser pemParser = new PEMParser(new StringReader(pem))) {
            Object object = pemParser.readObject();
            if (object instanceof PKCS10CertificationRequest) {
                return (PKCS10CertificationRequest) object;
            } else {
                throw new IllegalArgumentException("PEM content is not a CSR.");
            }
        }
    }
}
