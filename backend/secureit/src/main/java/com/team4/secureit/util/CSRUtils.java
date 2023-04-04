package com.team4.secureit.util;

import com.team4.secureit.dto.request.CSRCreationRequest;
import com.team4.secureit.model.CSRDetails;
import com.team4.secureit.model.User;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.x500.RDN;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemWriter;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.security.*;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.stream.Collectors;

public class CSRUtils {

    public static CSRDetails csrToCSRDetails(CSRCreationRequest request, KeyPair keyPair, PKCS10CertificationRequest csr, User subscriber) throws IOException {
        CSRDetails csrDetails = new CSRDetails();
        csrDetails.setCsrPem(csrToPEM(csr));
        csrDetails.setPrivateKeyPem(keyToPEM(keyPair.getPrivate()));
        csrDetails.setPublicKeyPem(keyToPEM(keyPair.getPublic()));
        csrDetails.setAlias(subscriber.getEmail() + getAliasSuffix());
        csrDetails.setCommonName(subscriber.getEmail());
        csrDetails.setOrganization(request.getOrganization());
        csrDetails.setCity(request.getCity());
        csrDetails.setState(request.getState());
        csrDetails.setCountry(request.getCountry());
        csrDetails.setAlgorithm(request.getAlgorithm());
        csrDetails.setKeySize(request.getKeySize());
        csrDetails.setSubscriber(subscriber);
        return csrDetails;
    }

    public static PKCS10CertificationRequest csrDetailsToCSR(CSRDetails csrDetails) throws IOException {
        return parseCSRFromPEM(csrDetails.getCsrPem());
    }

    public static String csrToPEM(PKCS10CertificationRequest csr) throws IOException {
        PemObject pemObject = new PemObject("CERTIFICATE REQUEST", csr.getEncoded());
        StringWriter stringWriter = new StringWriter();
        try (PemWriter pemWriter = new PemWriter(stringWriter)) {
            pemWriter.writeObject(pemObject);
        }
        return stringWriter.toString();
    }

    public static PKCS10CertificationRequest parseCSRFromPEM(String pem) throws IOException {
        try (PEMParser pemParser = new PEMParser(new StringReader(pem))) {
            Object object = pemParser.readObject();
            if (object instanceof PKCS10CertificationRequest) {
                return (PKCS10CertificationRequest) object;
            } else {
                throw new IllegalArgumentException("PEM content is not a CSR.");
            }
        }
    }

    public static String keyToPEM(Key key) throws IOException {
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

    private static String getAliasSuffix() {
        long unixTime = System.currentTimeMillis() / 1000L;
        return " " + Long.toHexString(unixTime);
    }
}
