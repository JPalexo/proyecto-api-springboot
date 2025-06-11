package com.j.c.proyecto.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) // 500 Internal Server Error
public class VentaException extends RuntimeException {
    public VentaException(String message) {
        super(message);
    }
}