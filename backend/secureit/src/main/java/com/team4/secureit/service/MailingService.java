package com.team4.secureit.service;

import com.google.zxing.WriterException;
import com.team4.secureit.config.AppProperties;
import com.team4.secureit.model.*;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.team4.secureit.util.LoginUtils.generateGoogleAuthenticatorLink;
import static com.team4.secureit.util.LoginUtils.generateQRCodeBase64;

@Service
public class MailingService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private LogService logService;

    private final Path templatesLocation;

    private final String senderAddress;

    @Autowired
    public MailingService(AppProperties appProperties) {
        templatesLocation = Paths.get(appProperties.getMailing().getTemplatesLocation());
        senderAddress = appProperties.getMailing().getSenderAddress();
    }

    @Async
    public void sendEmailVerificationMail(PropertyOwner propertyOwner) {
        String content = renderTemplate("verification.html",
                "firstName", propertyOwner.getFirstName(),
                "email", propertyOwner.getEmail(),
                "code", propertyOwner.getVerificationCode());

        sendMail(propertyOwner.getEmail(), "Welcome to Secure IT! Complete verification", content);
    }

    @Async
    public void sendTwoFactorSetupKey(User user) {
        try {
            String content = renderTemplate("2fa.html",
                    "firstName", user.getFirstName(),
                    "secretKey", user.getTwoFactorKey(),
                    "qrcode", generateQRCodeBase64(generateGoogleAuthenticatorLink(user)));

            sendMail(user.getEmail(), "Set up Two-Factor Authentication for Your Account", content);
        } catch (WriterException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Async
    public void sendInvitationToProperty(TenantInvite tenantInvite) {
        String content = renderTemplate("invitationToProperty.html",
                "firstName", tenantInvite.getUser().getFirstName(),
                "owner", tenantInvite.getProperty().getOwner().getFirstName() + " " + tenantInvite.getProperty().getOwner().getLastName(),
                "property", tenantInvite.getProperty().getName() + ", " + tenantInvite.getProperty().getAddress(),
                "code", tenantInvite.getVerificationCode());

        sendMail(tenantInvite.getUser().getEmail(), "Invitation to property", content);
    }

    private void sendMail(String to, String subject, String body) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setText(body, true);
            helper.setTo(to);
            helper.setFrom(senderAddress);
            helper.setSubject(subject);
            mailSender.send(mimeMessage);

            logService.log(
                    "An email with the subject + '" + subject + "' is sent to " + to,
                    LogSource.MAILING,
                    LogType.INFO
            );
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    private String renderTemplate(String templateName, String... variables) {
        Map<String, String> variableMap = new HashMap<>();

        List<String> keyValueList = Arrays.stream(variables).toList();

        if (keyValueList.size() % 2 != 0)
            throw new IllegalArgumentException();

        for (int i = 0; i < keyValueList.size(); i += 2) {
            variableMap.put(keyValueList.get(i), keyValueList.get(i + 1));
        }

        return renderTemplate(templateName, variableMap);
    }

    private String renderTemplate(String templateName, Map<String, String> variables) {
        File file = templatesLocation.resolve(templateName).toFile();
        String message = null;
        try {
            message = FileUtils.readFileToString(file, "UTF-8");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String target, renderedValue;
        for (var entry : variables.entrySet()) {
            target = "\\{\\{ " + entry.getKey() + " \\}\\}";
            renderedValue = entry.getValue();

            message = message.replaceAll(target, renderedValue);
        }

        return message;
    }


}
