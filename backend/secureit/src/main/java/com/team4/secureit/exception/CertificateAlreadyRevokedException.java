package com.team4.secureit.exception;

public class CertificateAlreadyRevokedException extends RuntimeException {
    public CertificateAlreadyRevokedException() {
    }

    public CertificateAlreadyRevokedException(String message) {
        super(message);
    }

    public CertificateAlreadyRevokedException(String message, Throwable cause) {
        super(message, cause);
    }
}
