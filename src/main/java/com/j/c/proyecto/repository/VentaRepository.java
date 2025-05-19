package com.j.c.proyecto.repository;

import com.j.c.proyecto.model.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Long> {
    // Podrías agregar métodos para consultas de reportes en el futuro
}