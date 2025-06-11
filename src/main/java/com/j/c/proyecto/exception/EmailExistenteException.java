package com.j.c.proyecto.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class EmailExistenteException extends RuntimeException {
    public EmailExistenteException(String email) {
        super("El email '" + email + "' ya está registrado");
    }
}