package com.team4.secureit.util;

import com.team4.secureit.model.CertificateDetails;
import org.bouncycastle.asn1.x509.KeyUsage;

import java.security.cert.X509Certificate;
import java.util.List;

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

    public static int getKeyUsageBitmap(List<String> keyUsageValues) {
        return keyUsageValues.stream()
                .mapToInt(CertificateUtils::getKeyUsageBit)
                .reduce(0, (a, b) -> a | b);
    }

    private static int getKeyUsageBit(String keyUsageValue) {
        return switch (keyUsageValue) {
            case "encipherOnly" -> KeyUsage.encipherOnly;
            case "cRLSign" -> KeyUsage.cRLSign;
            case "keyCertSign" -> KeyUsage.keyCertSign;
            case "keyAgreement" -> KeyUsage.keyAgreement;
            case "dataEncipherment" -> KeyUsage.dataEncipherment;
            case "keyEncipherment" -> KeyUsage.keyEncipherment;
            case "nonRepudiation" -> KeyUsage.nonRepudiation;
            case "digitalSignature" -> KeyUsage.digitalSignature;
            case "decipherOnly" -> KeyUsage.decipherOnly;
            default -> 0;
        };
    }

}