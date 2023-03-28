package com.team4.secureit.service;

import com.team4.secureit.config.AppProperties;
import com.team4.secureit.model.PropertyOwner;
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

@Service
public class MailingService {

    @Autowired
    private JavaMailSender mailSender;

    private final Path templatesLocation;

    private final String senderAddress;

    @Autowired
    public MailingService(AppProperties appProperties) {
        templatesLocation = Paths.get(appProperties.getMailing().getTemplatesLocation());
        senderAddress = appProperties.getMailing().getSenderAddress();
    }

    @Async
    public void sendEmailVerificationMail(PropertyOwner propertyOwner, String token) {
        String content = renderTemplate("verification.html",
                "firstName", propertyOwner.getFirstName(),
                "email", propertyOwner.getEmail(),
                "token", token);

        sendMail(propertyOwner.getEmail(), "Welcome to Secure IT! Complete verification", content);
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
