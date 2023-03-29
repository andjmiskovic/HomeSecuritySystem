package com.team4.secureit.certificates;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.Collection;

public class CertificateReader {
    public static final String BASE64_ENC_CERT_FILE = "./data/proba1.cer";
    public static final String BIN_ENC_CERT_FILE = "./data/proba.cer";

    public void testIt() {
        System.out.println("Cita sertifikat iz Base64 formata");
        readFromBase64EncFile();
        System.out.println("\n\nCita sertifikat iz binarnog formata");
        readFromBinEncFile();
    }

    private void readFromBase64EncFile() {
        try {
            FileInputStream fis = new FileInputStream(BASE64_ENC_CERT_FILE);
            BufferedInputStream bis = new BufferedInputStream(fis);

            CertificateFactory cf = CertificateFactory.getInstance("X.509");

            // Cita sertifikat po sertifikat
            // Svaki certifikat je izmedju
            // -----BEGIN CERTIFICATE-----,
            // i
            // -----END CERTIFICATE-----.
            while (bis.available() > 0) {
                Certificate cert = cf.generateCertificate(bis);
                System.out.println(cert.toString());
            }
        } catch (CertificateException | IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("rawtypes")
    private void readFromBinEncFile() {
        try {
            FileInputStream fis = new FileInputStream(BIN_ENC_CERT_FILE);
            CertificateFactory cf = CertificateFactory.getInstance("X.509");

            Collection c = cf.generateCertificates(fis);
            for (Object o : c) {
                Certificate cert = (Certificate) o;
                System.out.println(cert);
            }
        } catch (FileNotFoundException | CertificateException e) {
            e.printStackTrace();
        }
    }
}
