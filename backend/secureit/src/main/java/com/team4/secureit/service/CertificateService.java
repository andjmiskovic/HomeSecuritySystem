package com.team4.secureit.service;

import com.team4.secureit.dto.request.CertificateCreationOptions;
import com.team4.secureit.model.CertificateDetails;
import com.team4.secureit.repository.CertificateDetailsRepository;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.KeyStoreException;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.List;

@Service
public class CertificateService {

    @Autowired
    private CertificateDetailsRepository certificateDetailsRepository;

    @Autowired
    private KeyStoreService keyStoreService;

    public List<CertificateDetails> getAll() {
        return certificateDetailsRepository.findAll();
    }

    public List<CertificateDetails> findIssuerCertificates() {
        return certificateDetailsRepository.findIssuerCertificates();
    }

    public X509Certificate generateCertificate(PKCS10CertificationRequest csr, CertificateCreationOptions options) throws KeyStoreException, CertificateException, OperatorCreationException {
        SubjectPublicKeyInfo subjectPublicKeyInfo = csr.getSubjectPublicKeyInfo();
        X500Name subjectX500Name = csr.getSubject();

        String issuerAlias = options.getIssuerAlias();
        PrivateKey issuerPrivateKey = keyStoreService.getKeyPair(issuerAlias, "privatekeypassword".toCharArray()).getPrivate();
        X500Name issuerX500Name = new JcaX509CertificateHolder(keyStoreService.getCertificate(issuerAlias)).getSubject();

        BigInteger serialNumber = new BigInteger(128, new SecureRandom());
        Date notBefore = new Date();
        Date notAfter = new Date(System.currentTimeMillis() + 365L * 24L * 60L * 60L * 1000L);

        X509CertificateHolder certHolder = new X509v3CertificateBuilder(
                issuerX500Name,
                serialNumber,
                notBefore,
                notAfter,
                subjectX500Name,
                subjectPublicKeyInfo)
                .build(new JcaContentSignerBuilder("SHA256WithRSA").build(issuerPrivateKey));

        return new JcaX509CertificateConverter().getCertificate(certHolder);
    }
}
