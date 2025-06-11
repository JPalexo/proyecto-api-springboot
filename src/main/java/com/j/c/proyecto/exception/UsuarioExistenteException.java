package com.j.c.proyecto.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UsuarioExistenteException extends RuntimeException {
    public UsuarioExistenteException(String username) {
        super("El nombre de usuario '" + username + "' ya está en uso");
    }
}