package com.j.c.proyecto.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class FechaInvalidaException extends RuntimeException {
    public FechaInvalidaException(String message) {
        super(message);
    }
}