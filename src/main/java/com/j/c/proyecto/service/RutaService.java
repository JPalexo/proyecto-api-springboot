package com.j.c.proyecto.service;

import com.j.c.proyecto.dto.RutaDTO;
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
    public Ruta guardarRuta(RutaDTO rutaDTO) throws Exception {
        Optional<Ruta> rutaExistente = rutaRepository.findByCiudadAndNombreRuta(rutaDTO.getCiudad(), rutaDTO.getNombreRuta());
        if (rutaExistente.isPresent()) {
            throw new Exception("Ya existe una ruta con la misma ciudad y nombre.");
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
    public void eliminarRuta(Long id) throws Exception {
        Optional<Ruta> rutaOptional = rutaRepository.findById(id);
        if (rutaOptional.isPresent()) {
            // Aquí podrías agregar lógica para verificar si la ruta está siendo utilizada
            // por alguna tarifa antes de eliminarla, y lanzar una excepción si es así.
            rutaRepository.deleteById(id);
        } else {
            throw new Exception("No se encontró la ruta con el ID: " + id);
        }
    }
}