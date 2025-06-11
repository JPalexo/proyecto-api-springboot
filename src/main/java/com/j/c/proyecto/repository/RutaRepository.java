package com.j.c.proyecto.repository;

import com.j.c.proyecto.model.Ruta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RutaRepository extends JpaRepository<Ruta, Long> {
    Optional<Ruta> findByCiudadAndNombreRuta(String ciudad, String nombreRuta);
}