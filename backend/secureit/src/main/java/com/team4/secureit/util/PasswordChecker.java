package com.team4.secureit.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class PasswordChecker {
    private static final String COMMON_PASSWORDS_FILE = "resources/commonPasswords.txt";

    private final Set<String> passwords;

    public PasswordChecker() {
        this.passwords = loadCommonPasswords();
    }

    private Set<String> loadCommonPasswords() {
        Set<String> passwords = new HashSet<>();
        try (BufferedReader br = new BufferedReader(new FileReader(COMMON_PASSWORDS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                passwords.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return passwords;
    }

    public Boolean isCommonPassword(String password) {
        return passwords.contains(password);
    }
}
