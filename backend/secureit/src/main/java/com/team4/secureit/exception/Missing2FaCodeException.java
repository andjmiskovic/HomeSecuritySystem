package com.team4.secureit.exception;

import org.springframework.security.core.AuthenticationException;

public class Missing2FaCodeException extends AuthenticationException {

    public Missing2FaCodeException(String message) {
        super(message);
    }

    public Missing2FaCodeException(String message, Throwable cause) {
        super(message, cause);
    }
}
