package com.team4.secureit.service;

import com.team4.secureit.dto.request.DeviceMessage;
import com.team4.secureit.exception.DeviceNotFoundException;
import com.team4.secureit.model.Device;
import com.team4.secureit.repository.DeviceRepository;
import jakarta.xml.bind.DatatypeConverter;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.security.PublicKey;
import java.security.Security;
import java.security.Signature;
import java.security.spec.MGF1ParameterSpec;
import java.security.spec.PSSParameterSpec;
import java.util.UUID;

@Service
public class DeviceMonitoringService {

    @Autowired
    private DeviceRepository deviceRepository;

    public void processMessage(DeviceMessage message, String rawRequestBody, String signature) {
        UUID deviceId = message.getDeviceId();
        Device device = deviceRepository.findById(deviceId).orElseThrow(DeviceNotFoundException::new);

        boolean isSignatureValid = verifySignature(rawRequestBody, signature, device.getPublicKeyPem());
        if (isSignatureValid) {
            System.out.println(rawRequestBody);
        } else {
            System.out.println("Signature is not valid");
        }
    }

    public boolean verifySignature(String message, String signature, String publicKeyPem) {
        try {
            Security.addProvider(new BouncyCastleProvider());

            PEMParser pemParser = new PEMParser(new StringReader(publicKeyPem));
            Object object = pemParser.readObject();
            pemParser.close();

            JcaPEMKeyConverter converter = new JcaPEMKeyConverter().setProvider("BC");
            PublicKey publicKey = null;
            if (object instanceof SubjectPublicKeyInfo) {
                publicKey = converter.getPublicKey((SubjectPublicKeyInfo) object);
            }

            Signature publicSignature = Signature.getInstance("SHA256withRSA/PSS", "BC");
            publicSignature.setParameter(new PSSParameterSpec("SHA-256", "MGF1", MGF1ParameterSpec.SHA256, 20, 1));

            publicSignature.initVerify(publicKey);
            publicSignature.update(message.getBytes(StandardCharsets.UTF_8));
            byte[] signatureBytes = DatatypeConverter.parseHexBinary(signature);

            return publicSignature.verify(signatureBytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
