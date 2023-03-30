package com.team4.secureit.service;

import com.team4.secureit.dto.request.CertificateCreationOptions;
import com.team4.secureit.model.CertificateDetails;
import com.team4.secureit.repository.CertificateDetailsRepository;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.cert.CertIOException;
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
import java.util.Map;

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

    public X509Certificate generateCertificate(PKCS10CertificationRequest csr, CertificateCreationOptions options) throws KeyStoreException, CertificateException, OperatorCreationException, CertIOException {
        SubjectPublicKeyInfo subjectPublicKeyInfo = csr.getSubjectPublicKeyInfo();
        X500Name subjectX500Name = csr.getSubject();
        Map<String, String> extensions = options.getExtensions();

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

        // TODO: Fix implementation, this one does not follow X509
        for (Map.Entry<String, String> entry : extensions.entrySet()) {
            String objectId = entry.getKey();
            String value = entry.getValue();

            certBuilder.addExtension(new ASN1ObjectIdentifier(objectId), false, new DEROctetString(value.getBytes()));
        }

        X509CertificateHolder certHolder = certBuilder.build(new JcaContentSignerBuilder("SHA256WithRSA").build(issuerPrivateKey));

        return new JcaX509CertificateConverter().getCertificate(certHolder);
    }
}
