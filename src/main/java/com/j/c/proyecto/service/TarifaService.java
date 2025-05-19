package com.j.c.proyecto.service;

import com.j.c.proyecto.dto.TarifaDTO;
import com.j.c.proyecto.model.Ruta;
import com.j.c.proyecto.model.Tarifa;
import com.j.c.proyecto.repository.RutaRepository;
import com.j.c.proyecto.repository.TarifaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TarifaService {

    private final TarifaRepository tarifaRepository;
    private final RutaRepository rutaRepository;

    @Autowired
    public TarifaService(TarifaRepository tarifaRepository, RutaRepository rutaRepository) {
        this.tarifaRepository = tarifaRepository;
        this.rutaRepository = rutaRepository;
    }

    @Transactional
    public void guardarTarifa(TarifaDTO tarifaDTO) throws Exception {
        Ruta ruta = rutaRepository.findById(tarifaDTO.getRutaId())
                .orElseThrow(() -> new Exception("No se encontró la ruta con ID: " + tarifaDTO.getRutaId()));

        Optional<Tarifa> tarifaExistente = tarifaRepository.findByRuta(ruta);
        if (tarifaExistente.isPresent()) {
            throw new Exception("Ya existe una tarifa configurada para la ruta: " + ruta.getNombreRuta());
        }

        Tarifa nuevaTarifa = new Tarifa(ruta, tarifaDTO.getPrecio());
        tarifaRepository.save(nuevaTarifa);
    }

    public List<Tarifa> obtenerTodasLasTarifasOrdenadasPorRuta() {
        return tarifaRepository.findAllByOrderByRuta_NombreRutaAsc();
    }

    @Transactional
    public void actualizarTarifa(Long rutaId, java.math.BigDecimal nuevoPrecio) throws Exception {
        Ruta ruta = rutaRepository.findById(rutaId)
                .orElseThrow(() -> new Exception("No se encontró la ruta con ID: " + rutaId));

        Optional<Tarifa> tarifaOptional = tarifaRepository.findByRuta(ruta);
        if (tarifaOptional.isPresent()) {
            Tarifa tarifa = tarifaOptional.get();
            tarifa.setPrecio(nuevoPrecio);
            tarifaRepository.save(tarifa);
        } else {
            throw new Exception("No se encontró una tarifa configurada para la ruta: " + ruta.getNombreRuta());
        }
    }
}