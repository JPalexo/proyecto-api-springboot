package com.j.c.proyecto.service;

import com.j.c.proyecto.exception.*;
import com.j.c.proyecto.model.Ruta;
import com.j.c.proyecto.model.Descuento; // Importar Descuento
import com.j.c.proyecto.model.Venta;
import com.j.c.proyecto.repository.RutaRepository;
import com.j.c.proyecto.repository.VentaRepository;
import com.j.c.proyecto.repository.DescuentoRepository; // Importar DescuentoRepository
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class VentaService {

    private final VentaRepository ventaRepository;
    private final RutaRepository rutaRepository;
    private final DescuentoRepository descuentoRepository; // Inyectar DescuentoRepository
    private final DescuentoService descuentoService; // Inyectar DescuentoService para aplicar lógica

    @Autowired
    public VentaService(VentaRepository ventaRepository, RutaRepository rutaRepository,
                        DescuentoRepository descuentoRepository, DescuentoService descuentoService) {
        this.ventaRepository = ventaRepository;
        this.rutaRepository = rutaRepository;
        this.descuentoRepository = descuentoRepository;
        this.descuentoService = descuentoService;
    }

    @Transactional
    public Venta registrarVenta(Long rutaId, BigDecimal montoRecibido, Long descuentoId) { // Nuevo: descuentoId
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

        BigDecimal tarifaBase = ruta.getTarifas().get(0).getPrecio(); // Tarifa original de la ruta
        BigDecimal tarifaFinal = tarifaBase; // Tarifa que se cobrará, inicialmente la base

        Descuento descuentoAplicado = null;

        // Aplicar descuento si se proporcionó un ID
        if (descuentoId != null) {
            Optional<Descuento> descuentoOpt = descuentoRepository.findById(descuentoId);
            if (descuentoOpt.isPresent()) {
                descuentoAplicado = descuentoOpt.get();
                tarifaFinal = descuentoService.aplicarDescuento(tarifaBase, descuentoAplicado.getPorcentaje());
            } else {
                // Opcional: Podrías lanzar una excepción si el descuento no existe,
                // o simplemente ignorarlo si no es crítico. Para este caso, lo ignoramos.
            }
        }

        // Validar monto suficiente con la tarifa final (con o sin descuento)
        if (montoRecibido.compareTo(tarifaFinal) < 0) {
            throw new MontoInsuficienteException(montoRecibido, tarifaFinal);
        }

        BigDecimal cambio = montoRecibido.subtract(tarifaFinal);

        // Crear la venta usando la tarifa FINAL y el descuento aplicado (si lo hay)
        Venta venta = new Venta(ruta, tarifaFinal, montoRecibido, cambio); // Guardar la tarifa final
        // Puedes agregar el ID del descuento a la venta si modificas la entidad Venta
        // venta.setDescuentoAplicado(descuentoAplicado); // Requiere cambio en Venta model

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