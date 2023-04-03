package com.team4.secureit.service;

import com.team4.secureit.dto.request.CSRCreationRequest;
import com.team4.secureit.dto.request.CertificateCreationOptions;
import com.team4.secureit.exception.UserNotFoundException;
import com.team4.secureit.model.CSRDetails;
import com.team4.secureit.model.PropertyOwner;
import com.team4.secureit.model.RequestStatus;
import com.team4.secureit.model.User;
import com.team4.secureit.repository.CSRDetailsRepository;
import com.team4.secureit.repository.CertificateDetailsRepository;
import com.team4.secureit.repository.UserRepository;
import com.team4.secureit.util.CertificateUtils;
import jakarta.persistence.EntityNotFoundException;
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

import java.io.IOException;
import java.security.*;
import java.security.cert.X509Certificate;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static com.team4.secureit.util.CSRUtils.*;

@Service
public class CSRService {

    @Autowired
    private CertificateService certificateService;

    @Autowired
    private KeyStoreService keyStoreService;

    @Autowired
    private CSRDetailsRepository csrDetailsRepository;

    @Autowired
    private CertificateDetailsRepository certificateDetailsRepository;

    @Autowired
    private UserRepository userRepository;

    @SuppressWarnings("ConstantConditions")
    public CSRDetails generateAndPersistCSR(CSRCreationRequest request, User subscriber) throws OperatorCreationException, IOException {
        KeyPair keyPair = generateKeyPair(request.getAlgorithm(), request.getKeySize());
        PKCS10CertificationRequest csr = generateCSR(request, keyPair, subscriber.getEmail());
        CSRDetails csrDetails = csrToCSRDetails(request, keyPair, csr, subscriber);
        return csrDetailsRepository.save(csrDetails);
    }

    public List<CSRDetails> getAll() {
        return csrDetailsRepository.findAll();
    }

    public List<CSRDetails> findByStatusAndBySubscriber(String status, User subscriber) {
        return csrDetailsRepository.findAllBySubscriberIdAndStatus(subscriber.getId(), RequestStatus.valueOf(status.toUpperCase()));
    }

    public List<CSRDetails> getAllBySubscriber(User subscriber) {
        return csrDetailsRepository.findAllBySubscriberId(subscriber.getId());
    }

    public CSRDetails getById(UUID id) throws EntityNotFoundException {
        return csrDetailsRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("CSR not found."));
    }

    public List<CSRDetails> findByStatus(String status) {
        try {
            return csrDetailsRepository.findByStatus(RequestStatus.valueOf(status.toUpperCase()));
        } catch (IllegalArgumentException e) {
            return Collections.emptyList();
        }
    }

    public void issueAndPersistCertificate(UUID id, CertificateCreationOptions options) throws IOException, GeneralSecurityException, OperatorCreationException {
        CSRDetails csrDetails = getById(id);
        PKCS10CertificationRequest csr = csrDetailsToCSR(csrDetails);
        X509Certificate[] chain = keyStoreService.getCertificateChain(options.getIssuerAlias());

        String alias = csrDetails.getAlias();
        KeyPair keyPair = new KeyPair(
                parsePublicKeyFromPEM(csrDetails.getPublicKeyPem(), csrDetails.getAlgorithm()),
                parsePrivateKeyFromPEM(csrDetails.getPrivateKeyPem())
        );

        csrDetails.setStatus(RequestStatus.APPROVED);
        csrDetails.setProcessed(Instant.now());
        csrDetails.setPrivateKeyPem("REDACTED");
        csrDetailsRepository.save(csrDetails);

        User subscriber = csrDetails.getSubscriber();
        X509Certificate cert = certificateService.generateCertificate(csr, options);
        certificateDetailsRepository.save(CertificateUtils.convertToDetails(cert, alias, subscriber));

        keyStoreService.storeKeyPair(keyPair, alias, "keypassword".toCharArray(), chain);
    }

    public void rejectRequest(UUID id, String reason) {
        CSRDetails request = getById(id);

        request.setStatus(RequestStatus.REJECTED);
        request.setRejectionReason(reason);
        request.setProcessed(Instant.now());

        csrDetailsRepository.save(request);
    }

    private PKCS10CertificationRequest generateCSR(CSRCreationRequest request, KeyPair keyPair, String commonName) throws OperatorCreationException {
        Security.addProvider(new BouncyCastleProvider());

        X500Name x500Name = new X500NameBuilder()
                .addRDN(BCStyle.CN, commonName)
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


}
