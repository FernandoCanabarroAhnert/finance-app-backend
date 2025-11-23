package com.fernandocanabarro.finance_app_backend.shared.handler;

import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.support.WebExchangeBindException;

import com.fernandocanabarro.finance_app_backend.shared.exceptions.BadRequestException;
import com.fernandocanabarro.finance_app_backend.shared.exceptions.ForbiddenException;
import com.fernandocanabarro.finance_app_backend.shared.exceptions.NotFoundException;
import com.fernandocanabarro.finance_app_backend.shared.exceptions.dtos.StandardError;
import com.fernandocanabarro.finance_app_backend.shared.exceptions.dtos.ValidationError;

import reactor.core.publisher.Mono;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public Mono<ResponseEntity<StandardError>> badRequest(BadRequestException ex, ServerHttpRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return Mono.just(ResponseEntity.status(status).body(createStandardError(ex, status, request)));
    }

    @ExceptionHandler(ForbiddenException.class)
    public Mono<ResponseEntity<StandardError>> forbidden(ForbiddenException ex, ServerHttpRequest request) {
        HttpStatus status = HttpStatus.FORBIDDEN;
        return Mono.just(ResponseEntity.status(status).body(createStandardError(ex, status, request)));
    }

    @ExceptionHandler(NotFoundException.class)
    public Mono<ResponseEntity<StandardError>> notFound(NotFoundException ex, ServerHttpRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        return Mono.just(ResponseEntity.status(status).body(createStandardError(ex, status, request)));
    }

    @ExceptionHandler(WebExchangeBindException.class)
    public Mono<ResponseEntity<ValidationError>> invalidData(WebExchangeBindException ex, ServerHttpRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ValidationError err = new ValidationError(Instant.now(), status.value(), "Bad Request", "Invalid Data", request.getURI().getPath().toString());
        ex.getBindingResult().getFieldErrors().forEach(fieldError -> {
            err.addError(fieldError.getField(), fieldError.getDefaultMessage());
        });
        return Mono.just(ResponseEntity.status(status).body(err));
    }

    private StandardError createStandardError(Exception ex, HttpStatus status, ServerHttpRequest request) {
        return new StandardError(Instant.now(), status.value(), status.getReasonPhrase(), ex.getMessage(), request.getURI().getPath().toString());
    }

}
