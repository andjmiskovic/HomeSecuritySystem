package com.team4.secureit.service;

import com.team4.secureit.dto.request.CSRCreationRequest;
import com.team4.secureit.dto.request.CertificateCreationOptions;
import com.team4.secureit.model.PersistedCSR;
import com.team4.secureit.model.PropertyOwner;
import com.team4.secureit.model.RequestStatus;
import com.team4.secureit.repository.CertificateDetailsRepository;
import com.team4.secureit.repository.PersistedCSRRepository;
import com.team4.secureit.util.CertificateUtils;
import jakarta.persistence.EntityNotFoundException;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.x500.RDN;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;
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
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.security.*;
import java.security.cert.X509Certificate;
import java.security.spec.X509EncodedKeySpec;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
    public PersistedCSR generateAndPersistCSR(CSRCreationRequest request, PropertyOwner propertyOwner) throws OperatorCreationException, IOException {
        KeyPair keyPair = generateKeyPair(request.getAlgorithm(), request.getKeySize());
        PKCS10CertificationRequest csr = generateCSR(request, keyPair, propertyOwner.getEmail());
        PersistedCSR persistedCSR = pkcs10crToPersistedCSR(request, keyPair, csr);
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

    public void issueCertificate(UUID id, CertificateCreationOptions options) throws IOException, GeneralSecurityException, OperatorCreationException {
        PersistedCSR persistedCSR = getById(id);
        PKCS10CertificationRequest csr = persistedCSRtoPKCS10CR(persistedCSR);
        X509Certificate[] chain = keyStoreService.getCertificateChain(options.getIssuerAlias());

        String alias = persistedCSR.getAlias();
        KeyPair keyPair = new KeyPair(
                parsePublicKeyFromPEM(persistedCSR.getPublicKeyPem(), persistedCSR.getAlgorithm()),
                parsePrivateKeyFromPEM(persistedCSR.getPrivateKeyPem())
        );

        persistedCSR.setStatus(RequestStatus.APPROVED);
        persistedCSR.setProcessed(Instant.now());
        persistedCSR.setPrivateKeyPem("REDACTED");
        persistedCSRRepository.save(persistedCSR);

        X509Certificate cert = certificateService.generateCertificate(csr, options);
        certificateDetailsRepository.save(CertificateUtils.convertToDetails(cert, alias));

        keyStoreService.storeKeyPair(keyPair, alias, "keypassword".toCharArray(), chain);
    }

    public void rejectRequest(UUID id, String reason) {
        PersistedCSR request = getById(id);

        request.setStatus(RequestStatus.REJECTED);
        request.setRejectionReason(reason);
        request.setProcessed(Instant.now());

        persistedCSRRepository.save(request);
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

    private PersistedCSR pkcs10crToPersistedCSR(CSRCreationRequest request, KeyPair keyPair, PKCS10CertificationRequest csr) throws IOException {
        PersistedCSR persistedCSR = new PersistedCSR();
        persistedCSR.setCsrPem(csrToPEM(csr));
        persistedCSR.setPrivateKeyPem(keyToPEM(keyPair.getPrivate()));
        persistedCSR.setPublicKeyPem(keyToPEM(keyPair.getPublic()));
        persistedCSR.setAlias(readRDNsFromCSR(csr, BCStyle.CN));
        persistedCSR.setCommonName(readRDNsFromCSR(csr, BCStyle.CN));
        persistedCSR.setOrganization(request.getOrganization());
        persistedCSR.setCity(request.getCity());
        persistedCSR.setState(request.getState());
        persistedCSR.setCountry(request.getCountry());
        persistedCSR.setAlgorithm(request.getAlgorithm());
        persistedCSR.setKeySize(request.getKeySize());
        return persistedCSR;
    }

    private PKCS10CertificationRequest persistedCSRtoPKCS10CR(PersistedCSR persistedCSR) throws IOException {
        return parseCSRFromPEM(persistedCSR.getCsrPem());
    }

    private String csrToPEM(PKCS10CertificationRequest csr) throws IOException {
        PemObject pemObject = new PemObject("CERTIFICATE REQUEST", csr.getEncoded());
        StringWriter stringWriter = new StringWriter();
        try (PemWriter pemWriter = new PemWriter(stringWriter)) {
            pemWriter.writeObject(pemObject);
        }
        return stringWriter.toString();
    }

    private PKCS10CertificationRequest parseCSRFromPEM(String pem) throws IOException {
        try (PEMParser pemParser = new PEMParser(new StringReader(pem))) {
            Object object = pemParser.readObject();
            if (object instanceof PKCS10CertificationRequest) {
                return (PKCS10CertificationRequest) object;
            } else {
                throw new IllegalArgumentException("PEM content is not a CSR.");
            }
        }
    }

    private String keyToPEM(Key key) throws IOException {
        StringWriter stringWriter = new StringWriter();
        try (JcaPEMWriter pemWriter = new JcaPEMWriter(stringWriter)) {
            pemWriter.writeObject(key);
        }
        return stringWriter.toString();
    }

    public static PrivateKey parsePrivateKeyFromPEM(String pemString) throws IOException {
        Security.addProvider(new BouncyCastleProvider());
        Reader reader = new StringReader(pemString);
        PEMParser parser = new PEMParser(reader);

        PEMKeyPair pemKeyPair = (PEMKeyPair) parser.readObject();
        KeyPair keyPair = new JcaPEMKeyConverter().setProvider("BC").getKeyPair(pemKeyPair);
        return keyPair.getPrivate();
    }

    public static PublicKey parsePublicKeyFromPEM(String pemString, String algorithm) throws IOException, GeneralSecurityException {
        Security.addProvider(new BouncyCastleProvider());
        Reader reader = new StringReader(pemString);
        PEMParser parser = new PEMParser(reader);

        SubjectPublicKeyInfo subjectPublicKeyInfo = SubjectPublicKeyInfo.getInstance(parser.readObject());
        X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(subjectPublicKeyInfo.getEncoded());
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm, "BC");
        return keyFactory.generatePublic(publicKeySpec);
    }

    public static String readRDNsFromCSR(PKCS10CertificationRequest csr, ASN1ObjectIdentifier oid) {
        RDN[] rdns = csr.getSubject().getRDNs(oid);
        return Arrays.stream(rdns)
                .map(rdn -> rdn.getFirst().getValue().toString())
                .collect(Collectors.joining(","));
    }
}
