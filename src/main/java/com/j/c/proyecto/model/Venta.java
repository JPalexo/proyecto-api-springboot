package com.j.c.proyecto.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "ventas")
@Data
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ruta_id", nullable = false)
    private Ruta ruta;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal tarifa;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal montoRecibido;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal cambio;

    @Column(nullable = false)
    private LocalDateTime fechaVenta;

    @PrePersist
    public void prePersist() {
        fechaVenta = LocalDateTime.now();
    }

    public Venta() {
    }

    public Venta(Ruta ruta, BigDecimal tarifa, BigDecimal montoRecibido, BigDecimal cambio) {
        this.ruta = ruta;
        this.tarifa = tarifa;
        this.montoRecibido = montoRecibido;
        this.cambio = cambio;
        this.fechaVenta = LocalDateTime.now();
    }
}