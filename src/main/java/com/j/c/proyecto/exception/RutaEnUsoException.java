package com.j.c.proyecto.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST) // 400 Bad Request
public class RutaEnUsoException extends RuntimeException {
    public RutaEnUsoException(Long id) {
        super(String.format("La ruta con ID %d no puede ser eliminada porque está siendo utilizada", id));
    }
}