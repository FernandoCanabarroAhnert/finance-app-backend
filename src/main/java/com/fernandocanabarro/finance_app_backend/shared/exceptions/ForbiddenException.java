package com.fernandocanabarro.finance_app_backend.shared.exceptions;

public class ForbiddenException extends RuntimeException {
    public ForbiddenException(String message) {
        super(message);
    }
}
