package com.j.c.proyecto.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class RolInvalidoException extends RuntimeException {
    public RolInvalidoException(String rol) {
        super("Rol invalido para redirección: " + rol);
    }
}