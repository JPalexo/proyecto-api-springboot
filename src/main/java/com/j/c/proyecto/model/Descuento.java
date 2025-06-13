package com.j.c.proyecto.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "descuentos")
@Data
@NoArgsConstructor // Lombok para constructor sin argumentos
@AllArgsConstructor // Lombok para constructor con todos los argumentos
public class Descuento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String nombre; //"Estudiantil", "Tercera Edad", "Promocion Verano"

    // Porcentaje de descuento (ej. 0.10 para 10%, 0.25 para 25%)
    @Column(nullable = false, precision = 5, scale = 4) // 1.0000 para 100%, 0.2500 para 25%
    private BigDecimal porcentaje;

}