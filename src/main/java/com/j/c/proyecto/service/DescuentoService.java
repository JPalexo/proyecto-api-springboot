package com.j.c.proyecto.service;

import com.j.c.proyecto.dto.DescuentoDTO;
import com.j.c.proyecto.exception.DescuentoExistenteException;
import com.j.c.proyecto.exception.DescuentoNoEncontradoException;
import com.j.c.proyecto.model.Descuento;
import com.j.c.proyecto.repository.DescuentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class DescuentoService {

    private final DescuentoRepository descuentoRepository;

    @Autowired
    public DescuentoService(DescuentoRepository descuentoRepository) {
        this.descuentoRepository = descuentoRepository;
    }

    @Transactional
    public Descuento guardarDescuento(DescuentoDTO descuentoDTO) {
        // Verificar si ya existe un descuento con el mismo nombre
        Optional<Descuento> descuentoExistente = descuentoRepository.findByNombre(descuentoDTO.getNombre());
        if (descuentoExistente.isPresent()) {
            throw new DescuentoExistenteException("Ya existe un descuento con el nombre: " + descuentoDTO.getNombre());
        }

        Descuento nuevoDescuento = new Descuento();
        nuevoDescuento.setNombre(descuentoDTO.getNombre());
        nuevoDescuento.setPorcentaje(descuentoDTO.getPorcentaje());
        return descuentoRepository.save(nuevoDescuento);
    }

    public List<Descuento> obtenerTodosLosDescuentos() {
        return descuentoRepository.findAll();
    }

    public Optional<Descuento> obtenerDescuentoPorId(Long id) {
        return descuentoRepository.findById(id);
    }

    @Transactional
    public Descuento actualizarDescuento(Long id, DescuentoDTO descuentoDTO) {
        Descuento descuentoExistente = descuentoRepository.findById(id)
                .orElseThrow(() -> new DescuentoNoEncontradoException("Descuento con ID " + id + " no encontrado para actualizar."));

        // Verificar si el nuevo nombre ya existe en otro descuento (excepto si es el mismo descuento)
        Optional<Descuento> otroDescuentoConMismoNombre = descuentoRepository.findByNombre(descuentoDTO.getNombre());
        if (otroDescuentoConMismoNombre.isPresent() && !otroDescuentoConMismoNombre.get().getId().equals(id)) {
            throw new DescuentoExistenteException("Ya existe otro descuento con el nombre: " + descuentoDTO.getNombre());
        }

        descuentoExistente.setNombre(descuentoDTO.getNombre());
        descuentoExistente.setPorcentaje(descuentoDTO.getPorcentaje());
        return descuentoRepository.save(descuentoExistente);
    }

    @Transactional
    public void eliminarDescuento(Long id) {
        if (!descuentoRepository.existsById(id)) {
            throw new DescuentoNoEncontradoException("Descuento con ID " + id + " no encontrado para eliminar.");
        }
        // Nota: Si los descuentos se asocian a ventas históricas, considera
        // cómo manejar la eliminación (ej. ¿se permiten eliminaciones o solo deshabilitaciones?)
        descuentoRepository.deleteById(id);
    }

    /**
     * Aplica un descuento a una tarifa base.
     * @param tarifaBase La tarifa original del boleto.
     * @param porcentajeDescuento El porcentaje de descuento (ej. 0.10 para 10%).
     * @return La tarifa final con el descuento aplicado.
     */
    public BigDecimal aplicarDescuento(BigDecimal tarifaBase, BigDecimal porcentajeDescuento) {
        if (tarifaBase == null || porcentajeDescuento == null) {
            return tarifaBase; // O lanzar una excepción si prefieres
        }
        // Calcular el monto del descuento: tarifaBase * porcentajeDescuento
        BigDecimal montoDescuento = tarifaBase.multiply(porcentajeDescuento);
        // Calcular la tarifa final: tarifaBase - montoDescuento
        BigDecimal tarifaConDescuento = tarifaBase.subtract(montoDescuento);

        // Asegurarse de que la tarifa con descuento no sea negativa
        return tarifaConDescuento.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : tarifaConDescuento;
    }
}