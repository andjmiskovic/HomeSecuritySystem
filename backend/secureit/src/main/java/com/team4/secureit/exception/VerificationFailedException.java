package com.team4.secureit.exception;

import org.springframework.security.core.AuthenticationException;

public class VerificationFailedException extends AuthenticationException {
    public VerificationFailedException(String message) {
        super(message);
    }

    public VerificationFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}
