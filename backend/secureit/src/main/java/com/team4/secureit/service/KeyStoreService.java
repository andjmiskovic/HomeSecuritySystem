package com.team4.secureit.service;

import com.team4.secureit.model.CertificateDetails;
import com.team4.secureit.repository.CertificateDetailsRepository;
import com.team4.secureit.util.CertificateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Service
public class KeyStoreService {

    private final String KEYSTORE_PATH = "./keystore/main.keystore";
    private final char[] KEYSTORE_PASS = "example".toCharArray();

    private KeyStore keyStore;

    @Autowired
    private ApplicationContext context;

    @Autowired
    private CertificateDetailsRepository certificateDetailsRepository;

    public void init() {
        try {
            FileInputStream fis = new FileInputStream(KEYSTORE_PATH);
            keyStore = KeyStore.getInstance("JKS");
            keyStore.load(fis, KEYSTORE_PASS);

            syncDatabaseWithKeyStore(keyStore);
        } catch (CertificateException | KeyStoreException | IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
            SpringApplication.exit(context, () -> 1);
        }
    }

    public KeyPair getKeyPair(String alias, char[] keyPass) {
        try {
            PrivateKey privateKey = (PrivateKey) keyStore.getKey(alias, keyPass);
            PublicKey publicKey = getCertificate(alias).getPublicKey();
            return new KeyPair(publicKey, privateKey);
        } catch (KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException e) {
            return null;
        }
    }

    public X509Certificate getCertificate(String alias) throws KeyStoreException {
        return (X509Certificate) keyStore.getCertificate(alias);
    }

    public X509Certificate[] getCertificateChain(String alias, X509Certificate newCert) throws KeyStoreException {
        Certificate[] chain = keyStore.getCertificateChain(alias);
        X509Certificate[] x509chain = Arrays.stream(chain)
                .map(cert -> (X509Certificate) cert)
                .toArray(X509Certificate[]::new);
        return Stream.concat(Arrays.stream(x509chain), Stream.of(newCert))
                .toArray(X509Certificate[]::new);
    }

    public void storeKeyPair(KeyPair keyPair, String alias, char[] keyPass, X509Certificate[] chain) {
        try {
            PrivateKey privateKey = keyPair.getPrivate();
            keyStore.setKeyEntry(alias, privateKey, keyPass, chain);
            keyStore.store(new FileOutputStream(KEYSTORE_PATH), KEYSTORE_PASS);
        } catch (KeyStoreException | CertificateException | IOException | NoSuchAlgorithmException ignore) {

        }
    }

    private void syncDatabaseWithKeyStore(KeyStore keyStore) throws KeyStoreException {
        List<CertificateDetails> issuerCerts = Collections.list(keyStore.aliases())
                .stream()
                .map(alias -> {
                    try {
                        X509Certificate cert = (X509Certificate) keyStore.getCertificate(alias);
                        return CertificateUtils.convertToDetails(cert, alias, null);
                    } catch (KeyStoreException | CertificateEncodingException | IOException ignore) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .toList();

        certificateDetailsRepository.deleteAll();
        certificateDetailsRepository.saveAll(issuerCerts);
    }
}
