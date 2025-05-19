package com.j.c.proyecto.service;

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
    public void registrarVenta(Long rutaId, BigDecimal montoRecibido) throws Exception {
        Ruta ruta = rutaRepository.findById(rutaId)
                .orElseThrow(() -> new Exception("No se encontró la ruta con ID: " + rutaId));

        // Verificar si la ruta tiene una tarifa configurada (esto podría estar en TarifaService)
        if (ruta.getTarifas() == null || ruta.getTarifas().isEmpty()) {
            throw new Exception("La ruta " + ruta.getNombreRuta() + " no tiene una tarifa configurada.");
        }
        BigDecimal tarifa = ruta.getTarifas().get(0).getPrecio(); // Asumiendo una tarifa por ruta

        if (montoRecibido.compareTo(tarifa) < 0) {
            throw new Exception("El monto recibido es insuficiente.");
        }

        BigDecimal cambio = montoRecibido.subtract(tarifa);
        Venta venta = new Venta(ruta, tarifa, montoRecibido, cambio);
        ventaRepository.save(venta);
    }

    public List<Venta> obtenerTodasLasVentas() {
        return ventaRepository.findAll();
    }

    // Métodos para reportes de venta se agregarán aquí en el futuro
}