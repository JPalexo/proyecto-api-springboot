package com.j.c.proyecto.service;

import com.j.c.proyecto.exception.*;
import com.j.c.proyecto.model.Ruta;
import com.j.c.proyecto.model.Venta;
import com.j.c.proyecto.repository.RutaRepository;
import com.j.c.proyecto.repository.VentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class VentaService {

    private final VentaRepository ventaRepository;
    private final RutaRepository rutaRepository;

    @Autowired
    public VentaService(VentaRepository ventaRepository, RutaRepository rutaRepository) {
        this.ventaRepository = ventaRepository;
        this.rutaRepository = rutaRepository;
    }

    @Transactional
    public Venta registrarVenta(Long rutaId, BigDecimal montoRecibido) {
        // Validar monto positivo
        if (montoRecibido.compareTo(BigDecimal.ZERO) <= 0) {
            throw new MontoInsuficienteException(montoRecibido, BigDecimal.ZERO);
        }

        Ruta ruta = rutaRepository.findById(rutaId)
                .orElseThrow(() -> new RutaNoEncontradaException(rutaId));

        // Verificar tarifa configurada
        if (ruta.getTarifas() == null || ruta.getTarifas().isEmpty()) {
            throw new TarifaNoConfiguradaException(ruta.getNombreRuta());
        }

        BigDecimal tarifa = ruta.getTarifas().get(0).getPrecio();

        // Validar monto suficiente
        if (montoRecibido.compareTo(tarifa) < 0) {
            throw new MontoInsuficienteException(montoRecibido, tarifa);
        }

        BigDecimal cambio = montoRecibido.subtract(tarifa);
        Venta venta = new Venta(ruta, tarifa, montoRecibido, cambio);
        return ventaRepository.save(venta);
    }

    public List<Venta> obtenerVentasPorFecha(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        // Validar que fechaFin no sea anterior a fechaInicio
        if (fechaFin.isBefore(fechaInicio)) {
            throw new FechaInvalidaException("La fecha final no puede ser anterior a la fecha inicial");
        }

        return ventaRepository.findByFechaVentaBetween(fechaInicio, fechaFin);
    }
}