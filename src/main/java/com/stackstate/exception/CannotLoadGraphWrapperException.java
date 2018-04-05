package com.stackstate.exception;

public class CannotLoadGraphWrapperException extends Exception {

    private final String reason;

    private static final String message = "Cannot load graphWrapper, reason: ";


    public CannotLoadGraphWrapperException(String reason) {
        super(message + reason);
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }
}
