package com.j.c.proyecto.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT) // 409 Conflict
public class UsuarioEnUsoException extends RuntimeException {
    public UsuarioEnUsoException(Long id) {
        super(String.format("El usuario con ID %d no puede ser eliminado porque está asociado a registros existentes", id));
    }
}