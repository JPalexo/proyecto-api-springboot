package com.j.c.proyecto.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.math.BigDecimal;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PrecioInvalidoException extends RuntimeException {
    public PrecioInvalidoException(BigDecimal precio) {
        super(String.format("El precio %s no es válido. Debe ser mayor que cero", precio.toString()));
    }
}