package com.fernandocanabarro.finance_app_backend.shared.handler;

import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.support.WebExchangeBindException;

import com.fernandocanabarro.finance_app_backend.shared.exceptions.NotFoundException;
import com.fernandocanabarro.finance_app_backend.shared.exceptions.dtos.StandardError;
import com.fernandocanabarro.finance_app_backend.shared.exceptions.dtos.ValidationError;

import reactor.core.publisher.Mono;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public Mono<ResponseEntity<StandardError>> notFound(NotFoundException ex, ServerHttpRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        StandardError err = new StandardError(Instant.now(), status.value(), "Not Found", ex.getMessage(), request.getURI().getPath().toString());
        return Mono.just(ResponseEntity.status(status).body(err));
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

}
