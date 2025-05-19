package com.j.c.proyecto.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "rutas")
@Data
public class Ruta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String ciudad;

    @Column(nullable = false, length = 200)
    private String nombreRuta;

    @OneToMany(mappedBy = "ruta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Tarifa> tarifas;

    public Ruta() {
    }

    public Ruta(String ciudad, String nombreRuta) {
        this.ciudad = ciudad;
        this.nombreRuta = nombreRuta;
    }
}