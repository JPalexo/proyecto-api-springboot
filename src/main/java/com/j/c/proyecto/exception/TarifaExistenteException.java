package com.j.c.proyecto.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT) // 409 Conflict
public class TarifaExistenteException extends RuntimeException {
    public TarifaExistenteException(String nombreRuta) {
        super(String.format("Ya existe una tarifa configurada para la ruta: %s", nombreRuta));
    }
}