package com.j.c.proyecto.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class DescuentoDTO {

    private Long id; // Para casos de edición

    @NotBlank(message = "El nombre del descuento es obligatorio")
    @Size(max = 100, message = "El nombre del descuento no puede tener más de 100 caracteres")
    private String nombre;

    @NotNull(message = "El porcentaje de descuento es obligatorio")
    @DecimalMin(value = "0.01", message = "El porcentaje debe ser al menos 0.01")
    @DecimalMax(value = "1.00", message = "El porcentaje no puede ser mayor a 1.00 (100%)")
    private BigDecimal porcentaje; // Representado como decimal (ej: 0.10 para 10%)
}