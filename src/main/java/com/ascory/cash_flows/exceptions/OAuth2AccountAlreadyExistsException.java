package com.ascory.cash_flows.exceptions;

public class OAuth2AccountAlreadyExistsException extends RuntimeException {
    public OAuth2AccountAlreadyExistsException(String message) {
        super(message);
    }
}