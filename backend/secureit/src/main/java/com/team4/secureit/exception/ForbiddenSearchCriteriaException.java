package com.team4.secureit.exception;

public class ForbiddenSearchCriteriaException extends RuntimeException {
    public ForbiddenSearchCriteriaException() {
    }

    public ForbiddenSearchCriteriaException(String message) {
        super(message);
    }

    public ForbiddenSearchCriteriaException(String message, Throwable cause) {
        super(message, cause);
    }
}
