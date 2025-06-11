package com.j.c.proyecto.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND) // 404 Not Found
public class TarifaNoEncontradaException extends RuntimeException {
    public TarifaNoEncontradaException(String nombreRuta) {
        super(String.format("No se encontró una tarifa configurada para la ruta: %s", nombreRuta));
    }
}