package com.team4.secureit.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class PasswordChecker {
    private static final String COMMON_PASSWORDS_FILE = "resources/commonPasswords.txt";

    public static boolean isCommonPassword(String password) {
        try (BufferedReader br = new BufferedReader(new FileReader(COMMON_PASSWORDS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().equals(password)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
