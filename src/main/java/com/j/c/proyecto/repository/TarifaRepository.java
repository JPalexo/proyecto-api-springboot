package com.j.c.proyecto.repository;

import com.j.c.proyecto.model.Ruta;
import com.j.c.proyecto.model.Tarifa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface TarifaRepository extends JpaRepository<Tarifa, Long> {
    Optional<Tarifa> findByRuta(Ruta ruta); // Buscar tarifa por ruta
    List<Tarifa> findAllByOrderByRuta_NombreRutaAsc(); // Obtener tarifas ordenadas por nombre de ruta
    Optional<Object> findByRutaId(Long rutaId);
}