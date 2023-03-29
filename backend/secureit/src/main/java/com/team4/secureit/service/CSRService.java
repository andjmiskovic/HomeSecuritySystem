package com.team4.secureit.service;

import com.team4.secureit.dto.request.CSRCreationRequest;
import com.team4.secureit.dto.response.CSRResponse;
import com.team4.secureit.model.CertificateSigningRequest;
import com.team4.secureit.model.PersistedCSR;
import com.team4.secureit.model.RequestStatus;
import com.team4.secureit.repository.PersistedCSRRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.pkcs.jcajce.JcaPKCS10CertificationRequestBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class CSRService {

    @Autowired
    private PersistedCSRRepository persistedCSRRepository;

    public void generateAndPersistCSR(CSRCreationRequest request) throws OperatorCreationException {
        PKCS10CertificationRequest csr = generateCSR(request);
        PersistedCSR persistedCSR = convertToPersistedCSR(request, csr);
        persistedCSRRepository.save(persistedCSR);
    }

    @SuppressWarnings("ConstantConditions")
    private PKCS10CertificationRequest generateCSR(CSRCreationRequest request) throws OperatorCreationException {
        Security.addProvider(new BouncyCastleProvider());
        KeyPair keyPair = generateKeyPair(request.getAlgorithm(), request.getKeySize());

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

    private PersistedCSR convertToPersistedCSR(CSRCreationRequest request, PKCS10CertificationRequest csr) {
        PersistedCSR persistedCSR = new PersistedCSR();
        persistedCSR.setPem(csr.toString());
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

    private List<CSRResponse> csrMapper(List<PersistedCSR> requests) {
        List<CSRResponse> responses = new ArrayList<>();
        requests.forEach(request -> responses.add(new CSRResponse(request)));
        return responses;
    }

    public List<CSRResponse> getAll() {
        return csrMapper(persistedCSRRepository.findAll());
    }

    public List<CSRResponse> getByStatus(String status) {
        return csrMapper(persistedCSRRepository.findByStatus(RequestStatus.valueOf(status)));
    }

    public CSRResponse getById(UUID id) throws EntityNotFoundException {
        return new CSRResponse(get(id));
    }

    public CertificateSigningRequest get(UUID id) throws EntityNotFoundException {
        return csrRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public CSRResponse approve(UUID id, String adminEmail) {
        // TODO: Replace with PersitedCSR
        CertificateSigningRequest request = get(id);

        request.setStatus(RequestStatus.ACCEPTED);
        request.setProcessed(LocalDateTime.now());
        // TODO: create and save certificate
//        X509Certificate certificate = certificateService.generateCertificate(new SubjectData(), new IssuerData(), request);

        persistedCSRRepository.save(request);
        return new CSRResponse(request);
    }

    public CSRResponse reject(UUID id, String reason) {
        // TODO: Replace with PersitedCSR
        CertificateSigningRequest request = get(id);

        request.setStatus(RequestStatus.REJECTED);
        request.setRejectionReason(reason);
        request.setProcessed(LocalDateTime.now());

        persistedCSRRepository.save(request);
        return new CSRResponse(request);
    }
}
