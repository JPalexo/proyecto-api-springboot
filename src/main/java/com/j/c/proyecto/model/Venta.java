package com.j.c.proyecto.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "ventas")
@Data
@NoArgsConstructor // Lombok para constructor sin argumentos
@AllArgsConstructor // Lombok para constructor con todos los argumentos (aunque lo modificaremos)
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // Añadí LAZY para optimización
    @JoinColumn(name = "ruta_id", nullable = false)
    private Ruta ruta;

    // Ahora esta tarifa será la tarifa FINAL después de aplicar el descuento
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal tarifa;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal montoRecibido;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal cambio;

    @Column(nullable = false)
    private LocalDateTime fechaVenta;

    // Nuevo: Campo para el descuento aplicado (puede ser nulo si no se aplicó descuento)
    @ManyToOne(fetch = FetchType.LAZY) // Añadí LAZY para optimización
    @JoinColumn(name = "descuento_id", nullable = true) // nullable = true si el descuento es opcional
    private Descuento descuentoAplicado;

    @PrePersist
    public void prePersist() {
        // Asegúrate de que fechaVenta se establezca solo si no ha sido establecida manualmente
        if (fechaVenta == null) {
            fechaVenta = LocalDateTime.now();
        }
    }

    // Constructor actualizado para incluir el descuento
    // Lombok @AllArgsConstructor ya genera uno similar, pero este puede ser útil si manejas nulls
    public Venta(Ruta ruta, BigDecimal tarifa, BigDecimal montoRecibido, BigDecimal cambio, Descuento descuentoAplicado) {
        this.ruta = ruta;
        this.tarifa = tarifa;
        this.montoRecibido = montoRecibido;
        this.cambio = cambio;
        this.descuentoAplicado = descuentoAplicado;
        // La fechaVenta se establece con @PrePersist
    }

    // Mantén el constructor anterior si aún lo usas en algún lugar y no quieres romperlo inmediatamente
    public Venta(Ruta ruta, BigDecimal tarifa, BigDecimal montoRecibido, BigDecimal cambio) {
        this.ruta = ruta;
        this.tarifa = tarifa;
        this.montoRecibido = montoRecibido;
        this.cambio = cambio;
        this.descuentoAplicado = null; // Sin descuento
        // La fechaVenta se establece con @PrePersist
    }
}