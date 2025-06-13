package com.j.c.proyecto.service;

import com.j.c.proyecto.exception.*;
import com.j.c.proyecto.model.Ruta;
import com.j.c.proyecto.model.Descuento;
import com.j.c.proyecto.model.Venta;
import com.j.c.proyecto.repository.RutaRepository;
import com.j.c.proyecto.repository.VentaRepository;
import com.j.c.proyecto.repository.DescuentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value; // Importar Value para inyectar propiedades
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files; // Nuevo: Para manejar archivos y directorios
import java.nio.file.Path;  // Nuevo: Para manejar rutas de archivos
import java.nio.file.Paths; // Nuevo: Para construir rutas
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class VentaService {

    private final VentaRepository ventaRepository;
    private final RutaRepository rutaRepository;
    private final DescuentoRepository descuentoRepository;
    private final DescuentoService descuentoService;

    @Value("${app.ventas.ruta-archivos}") // Inyectamos la ruta desde application.properties
    private String ventasArchivosDir;

    @Autowired
    public VentaService(VentaRepository ventaRepository, RutaRepository rutaRepository,
                        DescuentoRepository descuentoRepository, DescuentoService descuentoService) {
        this.ventaRepository = ventaRepository;
        this.rutaRepository = rutaRepository;
        this.descuentoRepository = descuentoRepository;
        this.descuentoService = descuentoService;
    }

    @Transactional
    public Venta registrarVenta(Long rutaId, BigDecimal montoRecibido, Long descuentoId) {
        // ... (Tu lógica existente para registrar la venta) ...
        // No hay cambios aquí, es el mismo que el anterior que te di
        if (montoRecibido.compareTo(BigDecimal.ZERO) <= 0) {
            throw new MontoInsuficienteException(montoRecibido, BigDecimal.ZERO);
        }

        Ruta ruta = rutaRepository.findById(rutaId)
                .orElseThrow(() -> new RutaNoEncontradaException(rutaId));

        if (ruta.getTarifas() == null || ruta.getTarifas().isEmpty()) {
            throw new TarifaNoConfiguradaException(ruta.getNombreRuta());
        }

        BigDecimal tarifaBase = ruta.getTarifas().get(0).getPrecio();
        BigDecimal tarifaFinal = tarifaBase;

        Descuento descuentoAplicado = null;

        if (descuentoId != null) {
            Optional<Descuento> descuentoOpt = descuentoRepository.findById(descuentoId);
            if (descuentoOpt.isPresent()) {
                descuentoAplicado = descuentoOpt.get();
                tarifaFinal = descuentoService.aplicarDescuento(tarifaBase, descuentoAplicado.getPorcentaje());
            } else {
                System.err.println("Advertencia: Descuento con ID " + descuentoId + " no encontrado. No se aplicará descuento.");
            }
        }

        if (montoRecibido.compareTo(tarifaFinal) < 0) {
            throw new MontoInsuficienteException(montoRecibido, tarifaFinal);
        }

        BigDecimal cambio = montoRecibido.subtract(tarifaFinal);

        Venta venta = new Venta(ruta, tarifaFinal, montoRecibido, cambio, descuentoAplicado);
        Venta ventaGuardada = ventaRepository.save(venta);

        // Generar el archivo TXT después de que la venta se haya guardado y tenga el ID
        generarArchivoVenta(ventaGuardada, tarifaBase);

        return ventaGuardada;
    }

    public List<Venta> obtenerVentasPorFecha(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        if (fechaFin.isBefore(fechaInicio)) {
            throw new FechaInvalidaException("La fecha final no puede ser anterior a la fecha inicial");
        }
        return ventaRepository.findByFechaVentaBetween(fechaInicio, fechaFin);
    }

    /**
     * Genera un archivo TXT con los detalles de una venta.
     * El archivo se guarda en la ruta configurada en application.properties.
     * @param venta La venta registrada para generar el archivo.
     * @param tarifaOriginal La tarifa base de la ruta antes de cualquier descuento.
     */
    private void generarArchivoVenta(Venta venta, BigDecimal tarifaOriginal) {
        DateTimeFormatter fileFormatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
        DateTimeFormatter contentFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

        String fileName = "venta_" + venta.getId() + "_" + venta.getFechaVenta().format(fileFormatter) + ".txt";

        // Usamos Paths para construir la ruta y Files para crear el directorio y escribir el archivo
        Path directoryPath = Paths.get(ventasArchivosDir);
        Path filePath = directoryPath.resolve(fileName); // Combina el directorio con el nombre del archivo

        try {
            // Crear el directorio si no existe (incluye directorios padre)
            Files.createDirectories(directoryPath);
            System.out.println("Directorio de ventas verificado/creado: " + directoryPath.toAbsolutePath());

            // Escribir el contenido en el archivo
            try (BufferedWriter writer = Files.newBufferedWriter(filePath)) { // Files.newBufferedWriter maneja la creación/sobreescritura
                writer.write("--- Detalles de la Venta ---");
                writer.newLine();
                writer.write("ID de Venta: " + venta.getId());
                writer.newLine();
                writer.write("Fecha y Hora: " + venta.getFechaVenta().format(contentFormatter));
                writer.newLine();
                writer.write("Ruta: " + (venta.getRuta() != null ? venta.getRuta().getNombreRuta() : "N/A"));
                writer.newLine();
                writer.write("Tarifa Original (Base de Ruta): " + tarifaOriginal.toPlainString() + " MXN");
                writer.newLine();

                if (venta.getDescuentoAplicado() != null) {
                    writer.write("Descuento Aplicado: " + venta.getDescuentoAplicado().getNombre());
                    writer.newLine();
                    writer.write("Porcentaje de Descuento: " + venta.getDescuentoAplicado().getPorcentaje().multiply(new BigDecimal("100")).toPlainString() + "%");
                    writer.newLine();
                } else {
                    writer.write("Descuento Aplicado: Ninguno");
                    writer.newLine();
                }

                writer.write("Tarifa Final (con o sin descuento): " + venta.getTarifa().toPlainString() + " MXN");
                writer.newLine();
                writer.write("Monto Recibido: " + venta.getMontoRecibido().toPlainString() + " MXN");
                writer.newLine();
                writer.write("Cambio: " + venta.getCambio().toPlainString() + " MXN");
                writer.newLine();
                writer.write("----------------------------");

                System.out.println("Archivo de venta generado exitosamente: " + filePath.toAbsolutePath());
            }

        } catch (IOException e) {
            System.err.println("Error al escribir el archivo de venta " + fileName + ": " + e.getMessage());
            // En un entorno real, aquí usarías un logger (ej. LoggerFactory.getLogger(VentaService.class))
            // logger.error("Error al escribir el archivo de venta {}", fileName, e);
        }
    }
}