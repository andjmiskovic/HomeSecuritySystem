package com.team4.secureit.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.team4.secureit.model.User;
import de.taimos.totp.TOTP;
import org.apache.commons.codec.binary.Base32;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

public class LoginUtils {

    private static final SecureRandom random = new SecureRandom();

    public static String generateVerificationCode() {
        byte[] bytes = new byte[32];
        random.nextBytes(bytes);
        return Hex.encodeHexString(bytes);
    }

    public static String generateTwoFactorKey() {
        byte[] bytes = new byte[20];
        random.nextBytes(bytes);
        Base32 base32 = new Base32();
        return base32.encodeToString(bytes);
    }

    public static String getTOTPCode(String secretKey) {
        Base32 base32 = new Base32();
        byte[] bytes = base32.decode(secretKey);
        String hexKey = Hex.encodeHexString(bytes);
        return TOTP.getOTP(hexKey);
    }

    public static boolean verify2FA(String code, User user) {
        return code.equals(getTOTPCode(user.getTwoFactorKey()));
    }

    public static String generateGoogleAuthenticatorLink(User user) {
        String issuer = "Secure IT Inc.";
        String accountName = URLEncoder.encode(user.getFirstName() + " " + user.getLastName(), StandardCharsets.UTF_8);
        String secret = user.getTwoFactorKey();
        String algorithm = "SHA1";
        int digits = 6;
        int period = 30;

        return String.format("otpauth://totp/%s:%s?secret=%s&issuer=%s&algorithm=%s&digits=%d&period=%d",
                issuer, accountName, secret, issuer, algorithm, digits, period);
    }

    public static String generateQRCodeBase64(String url) throws WriterException, IOException {
        BitMatrix matrix = new MultiFormatWriter().encode(url, BarcodeFormat.QR_CODE, 200, 200);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(matrix, "png", bos);
        return Base64.encodeBase64String(bos.toByteArray());
    }

}
