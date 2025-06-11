package com.j.c.proyecto.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND) // 404 Not Found
public class RutaNoEncontradaException extends RuntimeException {
    public RutaNoEncontradaException(Long id) {
        super(String.format("No se encontró la ruta con ID: %d", id));
    }
}