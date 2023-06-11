package com.team4.secureit.exception;

public class RequestNotFoundException extends RuntimeException {
    public RequestNotFoundException() {
    }

    public RequestNotFoundException(String message) {
        super(message);
    }

    public RequestNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
