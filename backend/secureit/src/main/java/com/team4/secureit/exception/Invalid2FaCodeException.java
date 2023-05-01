package com.team4.secureit.exception;

import org.springframework.security.core.AuthenticationException;

public class Invalid2FaCodeException extends AuthenticationException {
    public Invalid2FaCodeException(String message) {
        super(message);
    }

    public Invalid2FaCodeException(String message, Throwable cause) {
        super(message, cause);
    }
}
