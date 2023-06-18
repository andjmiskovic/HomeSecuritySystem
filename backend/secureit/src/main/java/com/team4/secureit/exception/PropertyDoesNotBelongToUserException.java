package com.team4.secureit.exception;

public class PropertyDoesNotBelongToUserException  extends RuntimeException {
    public PropertyDoesNotBelongToUserException() {
    }

    public PropertyDoesNotBelongToUserException(String message) {
        super(message);
    }

    public PropertyDoesNotBelongToUserException(String message, Throwable cause) {
        super(message, cause);
    }
}
