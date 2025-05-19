package com.j.c.proyecto.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RutaDTO {

    @NotBlank(message = "La ciudad es obligatoria")
    @Size(max = 100, message = "La ciudad no puede tener más de 100 caracteres")
    private String ciudad;

    @NotBlank(message = "El nombre de la ruta es obligatorio")
    @Size(max = 200, message = "El nombre de la ruta no puede tener más de 200 caracteres")
    private String nombreRuta;
}