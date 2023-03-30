package com.team4.secureit.util;

import com.team4.secureit.model.CertificateDetails;

import java.security.cert.X509Certificate;

public class CertificateUtils {

    public static boolean isCA(X509Certificate cert) {
        return cert.getBasicConstraints() != -1; // -1 indicates not a CA cert, 0 and above indicates CA cert
    }

    public static CertificateDetails convertToDetails(X509Certificate cert, String alias) {
        return new CertificateDetails(
                cert.getSerialNumber(),
                alias,
                cert.getSubjectX500Principal().getName(),
                cert.getIssuerX500Principal().getName(),
                cert.getNotBefore(),
                cert.getNotAfter(),
                cert.getSigAlgName(),
                cert.getVersion(),
                cert.getPublicKey().getAlgorithm(),
                cert.getPublicKey().getFormat(),
                isCA(cert)
        );
    }

}