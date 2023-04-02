package com.team4.secureit.service;

import com.team4.secureit.dto.request.CertificateCreationOptions;
import com.team4.secureit.dto.request.CertificateRevocationRequest;
import com.team4.secureit.dto.request.Extensions;
import com.team4.secureit.dto.response.CertificateValidityResponse;
import com.team4.secureit.exception.CertificateAlreadyRevokedException;
import com.team4.secureit.model.CertificateDetails;
import com.team4.secureit.model.CertificateRevocation;
import com.team4.secureit.repository.CertificateDetailsRepository;
import com.team4.secureit.repository.CertificateRevocationRepository;
import jakarta.persistence.EntityNotFoundException;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.*;
import org.bouncycastle.cert.CertIOException;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509ExtensionUtils;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigInteger;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CertificateService {

    @Autowired
    private CertificateDetailsRepository certificateDetailsRepository;

    @Autowired
    private CertificateRevocationRepository certificateRevocationRepository;

    @Autowired
    private KeyStoreService keyStoreService;

    public List<CertificateDetails> getAll() {
        return certificateDetailsRepository.findAll();
    }

    public CertificateDetails getBySerialNumber(BigInteger serialNumber) {
        return certificateDetailsRepository.findBySerialNumber(serialNumber)
                .orElseThrow(() -> new EntityNotFoundException("Certificate with given serial number not found."));
    }

    public CertificateValidityResponse checkValidityForSerialNumber(BigInteger serialNumber) throws KeyStoreException {
        Optional<CertificateRevocation> revocation = certificateRevocationRepository.findBySerialNumber(serialNumber);
        if (revocation.isPresent())
            return new CertificateValidityResponse(revocation.get().getRevocationReason(), Date.from(revocation.get().getCreated()));

        CertificateDetails certificateDetails = getBySerialNumber(serialNumber);
        X509Certificate certificate = keyStoreService.getCertificate(certificateDetails.getAlias());

        Date validAfter = certificate.getNotBefore();
        Date validBefore = certificate.getNotAfter();
        try {
            certificate.checkValidity();
            return new CertificateValidityResponse(validAfter, validBefore);
        } catch (CertificateExpiredException e) {
            return new CertificateValidityResponse("The certificate has expired and is no longer valid.", validAfter, validBefore);
        } catch (CertificateNotYetValidException e) {
            return new CertificateValidityResponse("The certificate is not yet valid and cannot be used until its valid start date has passed.", validAfter, validBefore);
        } catch (Exception ignored) {
            return null;
        }
    }

    public void revokeCertificate(BigInteger serialNumber, CertificateRevocationRequest request) {
        if (certificateRevocationRepository.findBySerialNumber(serialNumber).isPresent())
            throw new CertificateAlreadyRevokedException();

        CertificateDetails certificateDetails = getBySerialNumber(serialNumber);
        CertificateRevocation certificateRevocation = new CertificateRevocation(certificateDetails.getSerialNumber(), request.getReason());
        certificateRevocationRepository.save(certificateRevocation);
    }

    public List<CertificateDetails> findIssuerCertificates() {
        return certificateDetailsRepository.findIssuerCertificates();
    }

    public List<CertificateRevocation> getAllRevocations() {
        return certificateRevocationRepository.findAll();
    }

    public X509Certificate generateCertificate(PKCS10CertificationRequest csr, CertificateCreationOptions options) throws KeyStoreException, CertificateException, OperatorCreationException, IOException, NoSuchAlgorithmException {
        SubjectPublicKeyInfo subjectPublicKeyInfo = csr.getSubjectPublicKeyInfo();
        X500Name subjectX500Name = csr.getSubject();

        String issuerAlias = options.getIssuerAlias();
        PrivateKey issuerPrivateKey = keyStoreService.getKeyPair(issuerAlias, "privatekeypassword".toCharArray()).getPrivate();
        X500Name issuerX500Name = new JcaX509CertificateHolder(keyStoreService.getCertificate(issuerAlias)).getSubject();

        BigInteger serialNumber = new BigInteger(64, new SecureRandom());
        Date notBefore = new Date();
        Date notAfter = new Date(System.currentTimeMillis() + 365L * 24L * 60L * 60L * 1000L);

        X509v3CertificateBuilder certBuilder = new X509v3CertificateBuilder(
                issuerX500Name,
                serialNumber,
                notBefore,
                notAfter,
                subjectX500Name,
                subjectPublicKeyInfo);

        setExtensions(certBuilder, options.getExtensions(), subjectPublicKeyInfo);

        X509CertificateHolder certHolder = certBuilder.build(new JcaContentSignerBuilder("SHA256WithRSA").build(issuerPrivateKey));

        return new JcaX509CertificateConverter().getCertificate(certHolder);
    }

    private void setExtensions(X509v3CertificateBuilder certBuilder, Extensions extensions, SubjectPublicKeyInfo subjectPublicKeyInfo) throws IOException, NoSuchAlgorithmException {
        // Set keyUsage
        int keyUsageBitmap = 0;
        List<String> keyUsageValues = extensions.getKeyUsage();
        if (keyUsageValues.contains("encipherOnly"))
            keyUsageBitmap |= KeyUsage.encipherOnly;
        if (keyUsageValues.contains("cRLSign"))
            keyUsageBitmap |= KeyUsage.cRLSign;
        if (keyUsageValues.contains("keyCertSign"))
            keyUsageBitmap |= KeyUsage.keyCertSign;
        if (keyUsageValues.contains("keyAgreement"))
            keyUsageBitmap |= KeyUsage.keyAgreement;
        if (keyUsageValues.contains("dataEncipherment"))
            keyUsageBitmap |= KeyUsage.dataEncipherment;
        if (keyUsageValues.contains("keyEncipherment"))
            keyUsageBitmap |= KeyUsage.keyEncipherment;
        if (keyUsageValues.contains("nonRepudiation"))
            keyUsageBitmap |= KeyUsage.nonRepudiation;
        if (keyUsageValues.contains("digitalSignature"))
            keyUsageBitmap |= KeyUsage.digitalSignature;
        if (keyUsageValues.contains("decipherOnly"))
            keyUsageBitmap |= KeyUsage.decipherOnly;
        certBuilder.addExtension(
                Extension.keyUsage,
                true,
                new KeyUsage(keyUsageBitmap)
        );

        // Set subjectAlternativeName
        if (extensions.getSubjectAlternativeName() != null) {
            List<String> altNames = extensions.getSubjectAlternativeName();
            GeneralNames sanNames = new GeneralNames(
                    altNames.stream()
                            .map(e -> new GeneralName(GeneralName.dNSName, e))
                            .toArray(GeneralName[]::new)
            );

            certBuilder.addExtension(
                    Extension.subjectAlternativeName,
                    false,
                    sanNames
            );
        }

        // Set subjectKeyIdentifier
        if (extensions.getSubjectKeyIdentifier() != null && extensions.getSubjectKeyIdentifier()) {
            byte[] subjectKeyIdentifier = new SubjectKeyIdentifier(subjectPublicKeyInfo.getPublicKeyData().getBytes()).getKeyIdentifier();
            certBuilder.addExtension(
                    Extension.subjectKeyIdentifier,
                    true,
                    new SubjectKeyIdentifier(subjectKeyIdentifier).getKeyIdentifier()
            );
        }

        // Set authorityKeyIdentifier
        if (extensions.getAuthorityKeyIdentifier() != null && extensions.getAuthorityKeyIdentifier()) {
            JcaX509ExtensionUtils extensionUtils = new JcaX509ExtensionUtils();
            byte[] authorityKeyIdentifier = extensionUtils.createAuthorityKeyIdentifier(subjectPublicKeyInfo).getKeyIdentifier();
            certBuilder.addExtension(
                    Extension.authorityKeyIdentifier,
                    false,
                    new AuthorityKeyIdentifier(authorityKeyIdentifier)
            );
        }
    }
}
