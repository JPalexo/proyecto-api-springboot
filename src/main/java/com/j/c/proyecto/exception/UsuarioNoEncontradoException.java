package com.j.c.proyecto.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND) // 404 Not Found
public class UsuarioNoEncontradoException extends RuntimeException {
    public UsuarioNoEncontradoException(Long id) {
        super(String.format("No se encontró el usuario con ID: %d", id));
    }
}
