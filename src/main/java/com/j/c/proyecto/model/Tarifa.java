package com.j.c.proyecto.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Table(name = "tarifas")
@Data
public class Tarifa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ruta_id", nullable = false)
    private Ruta ruta;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;

    public Tarifa() {
    }

    public Tarifa(Ruta ruta, BigDecimal precio) {
        this.ruta = ruta;
        this.precio = precio;
    }
}