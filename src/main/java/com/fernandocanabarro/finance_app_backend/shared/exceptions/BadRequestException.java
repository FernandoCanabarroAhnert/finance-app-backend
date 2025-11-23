package com.fernandocanabarro.finance_app_backend.shared.exceptions;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}
