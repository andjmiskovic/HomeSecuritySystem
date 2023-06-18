package com.team4.secureit.exception;

public class BlacklistedTokenException extends InvalidAccessTokenException {
    public BlacklistedTokenException(String message) {
        super(message);
    }

    public BlacklistedTokenException(String message, Throwable cause) {
        super(message, cause);
    }
}
