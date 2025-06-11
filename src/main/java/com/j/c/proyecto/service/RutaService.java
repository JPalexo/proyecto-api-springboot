package com.j.c.proyecto.service;

import com.j.c.proyecto.dto.RutaDTO;
import com.j.c.proyecto.exception.RutaEnUsoException;
import com.j.c.proyecto.exception.RutaExistenteException;
import com.j.c.proyecto.exception.RutaNoEncontradaException;
import com.j.c.proyecto.model.Ruta;
import com.j.c.proyecto.repository.RutaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class RutaService {

    private final RutaRepository rutaRepository;

    @Autowired
    public RutaService(RutaRepository rutaRepository) {
        this.rutaRepository = rutaRepository;
    }

    @Transactional
    public Ruta guardarRuta(RutaDTO rutaDTO) {
        Optional<Ruta> rutaExistente = rutaRepository.findByCiudadAndNombreRuta(
                rutaDTO.getCiudad(),
                rutaDTO.getNombreRuta()
        );

        if (rutaExistente.isPresent()) {
            throw new RutaExistenteException(
                    rutaDTO.getCiudad(),
                    rutaDTO.getNombreRuta()
            );
        }

        Ruta nuevaRuta = new Ruta();
        nuevaRuta.setCiudad(rutaDTO.getCiudad());
        nuevaRuta.setNombreRuta(rutaDTO.getNombreRuta());
        return rutaRepository.save(nuevaRuta);
    }

    public List<Ruta> obtenerTodasLasRutas() {
        return rutaRepository.findAll();
    }

    @Transactional
    public void eliminarRuta(Long id) {
        Ruta ruta = rutaRepository.findById(id)
                .orElseThrow(() -> new RutaNoEncontradaException(id));

        // Verificar si la ruta está siendo utilizada
        if (rutaEstaEnUso(ruta)) {
            throw new RutaEnUsoException(id);
        }

        rutaRepository.delete(ruta);
    }

    private boolean rutaEstaEnUso(Ruta ruta) {
        // Implementa la lógica para verificar si la ruta está siendo usada
        // Por ejemplo, verificar si hay ventas o tarifas asociadas
        // return ventaRepository.existsByRuta(ruta) || tarifaRepository.existsByRuta(ruta);
        return false; // Cambiar por implementación real
    }
}