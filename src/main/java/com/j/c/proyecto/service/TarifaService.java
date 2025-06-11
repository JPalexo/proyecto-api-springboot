package com.j.c.proyecto.service;

import com.j.c.proyecto.dto.TarifaDTO;
import com.j.c.proyecto.exception.PrecioInvalidoException;
import com.j.c.proyecto.exception.RutaNoEncontradaException;
import com.j.c.proyecto.exception.TarifaExistenteException;
import com.j.c.proyecto.exception.TarifaNoEncontradaException;
import com.j.c.proyecto.model.Ruta;
import com.j.c.proyecto.model.Tarifa;
import com.j.c.proyecto.repository.RutaRepository;
import com.j.c.proyecto.repository.TarifaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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
    public void guardarTarifa(TarifaDTO tarifaDTO) {
        if (tarifaDTO.getPrecio().compareTo(BigDecimal.ZERO) <= 0) {
            throw new PrecioInvalidoException(tarifaDTO.getPrecio());
        }
        Ruta ruta = rutaRepository.findById(tarifaDTO.getRutaId())
                .orElseThrow(() -> new RutaNoEncontradaException(tarifaDTO.getRutaId()));

        Optional<Tarifa> tarifaExistente = tarifaRepository.findByRuta(ruta);
        if (tarifaExistente.isPresent()) {
            throw new TarifaExistenteException(ruta.getNombreRuta());
        }

        Tarifa nuevaTarifa = new Tarifa(ruta, tarifaDTO.getPrecio());
        tarifaRepository.save(nuevaTarifa);
    }

    public List<Tarifa> obtenerTodasLasTarifasOrdenadasPorRuta() {
        return tarifaRepository.findAllByOrderByRuta_NombreRutaAsc();
    }

    @Transactional
    public void actualizarTarifa(Long rutaId, java.math.BigDecimal nuevoPrecio) {
        Ruta ruta = rutaRepository.findById(rutaId)
                .orElseThrow(() -> new RutaNoEncontradaException(rutaId));

        Tarifa tarifa = tarifaRepository.findByRuta(ruta)
                .orElseThrow(() -> new TarifaNoEncontradaException(ruta.getNombreRuta()));

        tarifa.setPrecio(nuevoPrecio);
        tarifaRepository.save(tarifa);
    }
}