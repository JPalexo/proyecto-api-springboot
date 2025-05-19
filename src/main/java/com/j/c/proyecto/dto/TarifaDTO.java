package com.j.c.proyecto.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class TarifaDTO {

    @NotNull(message = "La ID de la ruta es obligatoria")
    private Long rutaId;

    @NotNull(message = "El precio es obligatorio")
    @Positive(message = "El precio debe ser un valor positivo")
    private java.math.BigDecimal precio;
}