package com.team4.secureit.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class NotCommonValidator implements ConstraintValidator<NotCommon, String> {

    private static final String COMMON_PASSWORDS_FILE = "/commonPasswords.txt";

    private Set<String> passwords;

    @Override
    public void initialize(NotCommon constraintAnnotation) {
        passwords = loadCommonPasswords();
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext constraintValidatorContext) {
        return !passwords.contains(password);
    }

    @SuppressWarnings("ConstantConditions")
    private Set<String> loadCommonPasswords() {
        Set<String> passwords = new HashSet<>();
        String filePath = NotCommonValidator.class.getResource(COMMON_PASSWORDS_FILE).getPath();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                passwords.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return passwords;
    }

}
