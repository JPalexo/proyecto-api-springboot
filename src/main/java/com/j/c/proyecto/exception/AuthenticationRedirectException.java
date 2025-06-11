package com.j.c.proyecto.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class AuthenticationRedirectException extends RuntimeException {
    public AuthenticationRedirectException(String message, Throwable cause) {
        super("Error durante la redirección post-autenticación: " + message, cause);
    }
}