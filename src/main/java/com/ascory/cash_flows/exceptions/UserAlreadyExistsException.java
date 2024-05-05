package com.ascory.cash_flows.exceptions;

public class UserAlreadyExistsException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "User already exists.";

    public UserAlreadyExistsException() {
        super(DEFAULT_MESSAGE);
    }

    public UserAlreadyExistsException(String message) {
        super(DEFAULT_MESSAGE + " " + message);
    }
}
