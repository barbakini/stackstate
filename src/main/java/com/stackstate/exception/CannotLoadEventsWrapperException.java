package com.stackstate.exception;

public class CannotLoadEventsWrapperException extends Exception {

    private final String reason;

    private static final String message = "Cannot load eventsWrapper, reason: ";


    public CannotLoadEventsWrapperException(String reason) {
        super(message + reason);
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }
}
