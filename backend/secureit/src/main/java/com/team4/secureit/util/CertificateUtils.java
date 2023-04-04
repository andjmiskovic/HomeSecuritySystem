package com.team4.secureit.util;

import com.team4.secureit.model.CertificateDetails;
import com.team4.secureit.model.User;
import org.bouncycastle.asn1.x509.KeyUsage;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemWriter;

import java.io.IOException;
import java.io.StringWriter;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.List;

public class CertificateUtils {

    public static boolean isCA(X509Certificate cert) {
        return cert.getBasicConstraints() != -1; // -1 indicates not a CA cert, 0 and above indicates CA cert
    }

    public static CertificateDetails convertToDetails(X509Certificate cert, String alias, User subscriber) throws CertificateEncodingException, IOException {
        return new CertificateDetails(
                cert.getSerialNumber(),
                certToPEM(cert),
                alias,
                cert.getSubjectX500Principal().getName(),
                cert.getIssuerX500Principal().getName(),
                cert.getNotBefore(),
                cert.getNotAfter(),
                cert.getSigAlgName(),
                cert.getVersion(),
                cert.getPublicKey().getAlgorithm(),
                cert.getPublicKey().getFormat(),
                isCA(cert),
                subscriber
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

    public static String certToPEM(X509Certificate cert) throws IOException, CertificateEncodingException {
        PemObject pemObject = new PemObject("CERTIFICATE", cert.getEncoded());
        StringWriter stringWriter = new StringWriter();
        try (PemWriter pemWriter = new PemWriter(stringWriter)) {
            pemWriter.writeObject(pemObject);
        }
        return stringWriter.toString();
    }

}