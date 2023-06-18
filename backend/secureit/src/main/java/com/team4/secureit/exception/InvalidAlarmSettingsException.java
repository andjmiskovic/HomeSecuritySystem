package com.team4.secureit.exception;

public class InvalidAlarmSettingsException extends RuntimeException {
    public InvalidAlarmSettingsException() {
    }

    public InvalidAlarmSettingsException(String message) {
        super(message);
    }

    public InvalidAlarmSettingsException(String message, Throwable cause) {
        super(message, cause);
    }
}
