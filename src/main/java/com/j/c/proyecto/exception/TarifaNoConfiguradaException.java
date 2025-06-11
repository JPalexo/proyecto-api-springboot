package com.j.c.proyecto.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST) // 400 Bad Request
public class TarifaNoConfiguradaException extends RuntimeException {
    public TarifaNoConfiguradaException(String nombreRuta) {
        super(String.format("La ruta '%s' no tiene una tarifa configurada", nombreRuta));
    }
}