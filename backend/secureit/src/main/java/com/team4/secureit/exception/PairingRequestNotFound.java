package com.team4.secureit.exception;

public class PairingRequestNotFound extends RuntimeException {
    public PairingRequestNotFound() {
    }

    public PairingRequestNotFound(String message) {
        super(message);
    }

    public PairingRequestNotFound(String message, Throwable cause) {
        super(message, cause);
    }
}
