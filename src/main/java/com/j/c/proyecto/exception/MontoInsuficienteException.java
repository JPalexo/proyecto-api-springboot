package com.j.c.proyecto.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.math.BigDecimal;

@ResponseStatus(HttpStatus.BAD_REQUEST) // 400 Bad Request
public class MontoInsuficienteException extends RuntimeException {
    public MontoInsuficienteException(BigDecimal montoRecibido, BigDecimal tarifa) {
        super(String.format("El monto recibido %s es insuficiente para cubrir la tarifa %s",
                montoRecibido.toString(), tarifa.toString()));
    }
}