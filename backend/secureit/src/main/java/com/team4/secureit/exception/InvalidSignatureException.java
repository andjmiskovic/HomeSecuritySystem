package com.team4.secureit.exception;

public class InvalidSignatureException extends RuntimeException {
    public InvalidSignatureException() {
    }

    public InvalidSignatureException(String message) {
        super(message);
    }

    public InvalidSignatureException(String message, Throwable cause) {
        super(message, cause);
    }
}
