package com.j.c.proyecto.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT) // 409 Conflict
public class RutaExistenteException extends RuntimeException {
    public RutaExistenteException(String ciudad, String nombreRuta) {
        super(String.format("Ya existe una ruta con la ciudad '%s' y nombre '%s'", ciudad, nombreRuta));
    }
}