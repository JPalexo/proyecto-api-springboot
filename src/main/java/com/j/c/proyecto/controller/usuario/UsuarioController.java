package com.j.c.proyecto.controller.usuario;

import com.j.c.proyecto.model.Ruta;
import com.j.c.proyecto.model.Descuento; // Importar Descuento
import com.j.c.proyecto.service.RutaService;
import com.j.c.proyecto.service.VentaService;
import com.j.c.proyecto.service.DescuentoService; // Importar DescuentoService
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class UsuarioController {

    private final RutaService rutaService;
    private final VentaService ventaService;
    private final DescuentoService descuentoService; // Inyectar DescuentoService

    @Autowired
    public UsuarioController(RutaService rutaService, VentaService ventaService, DescuentoService descuentoService) {
        this.rutaService = rutaService;
        this.ventaService = ventaService;
        this.descuentoService = descuentoService; // Inicializar
    }

    @GetMapping("/usuario/venta")
    public String mostrarPaginaVenta(Model model, @RequestParam(value = "ciudadSeleccionada", required = false) String ciudadSeleccionada,
                                     @RequestParam(value = "rutaSeleccionadaId", required = false) Long rutaSeleccionadaId,
                                     @RequestParam(value = "descuentoSeleccionadoId", required = false) Long descuentoSeleccionadoId) { // Nuevo: descuentoId

        List<String> ciudades = rutaService.obtenerTodasLasRutas().stream()
                .map(Ruta::getCiudad)
                .distinct()
                .collect(Collectors.toList());
        model.addAttribute("ciudades", ciudades);
        model.addAttribute("ciudadSeleccionada", ciudadSeleccionada);
        model.addAttribute("rutaSeleccionadaId", rutaSeleccionadaId);
        model.addAttribute("descuentoSeleccionadoId", descuentoSeleccionadoId); // Pasar al modelo

        List<Ruta> rutas = null;
        if (ciudadSeleccionada != null) {
            rutas = rutaService.obtenerTodasLasRutas().stream()
                    .filter(ruta -> ruta.getCiudad().equals(ciudadSeleccionada))
                    .collect(Collectors.toList());
        }
        model.addAttribute("rutas", rutas);

        // Cargar descuentos para el select
        List<Descuento> descuentos = descuentoService.obtenerTodosLosDescuentos();
        model.addAttribute("descuentos", descuentos);

        // Inicializar tarifa y cambio
        model.addAttribute("tarifa", null);
        model.addAttribute("cambio", null);
        model.addAttribute("tarifaConDescuento", null); // Nuevo: Tarifa final con descuento
        return "usuario/venta";
    }

    // --- Métodos existentes, modificados para pasar el descuentoId ---

    @PostMapping("/usuario/obtener-rutas")
    public String obtenerRutasPorCiudad(@RequestParam("ciudadSeleccionada") String ciudad, Model model) {
        List<String> ciudades = rutaService.obtenerTodasLasRutas().stream()
                .map(Ruta::getCiudad)
                .distinct()
                .collect(Collectors.toList());
        List<Ruta> rutas = rutaService.obtenerTodasLasRutas().stream()
                .filter(ruta -> ruta.getCiudad().equals(ciudad))
                .collect(Collectors.toList());
        model.addAttribute("ciudades", ciudades);
        model.addAttribute("rutas", rutas);
        model.addAttribute("ciudadSeleccionada", ciudad);
        model.addAttribute("rutaSeleccionadaId", null);
        model.addAttribute("descuentoSeleccionadoId", null); // Resetear descuento al cambiar ciudad
        model.addAttribute("tarifa", null);
        model.addAttribute("cambio", null);
        model.addAttribute("tarifaConDescuento", null); // Resetear
        model.addAttribute("descuentos", descuentoService.obtenerTodosLosDescuentos()); // Recargar descuentos
        return "usuario/venta";
    }

    @PostMapping("/usuario/obtener-tarifa")
    public String obtenerTarifaPorRuta(@RequestParam("rutaSeleccionada") Long rutaId,
                                       @RequestParam("ciudadSeleccionada") String ciudadSeleccionada,
                                       @RequestParam(value = "descuentoSeleccionado", required = false) Long descuentoId, // Nuevo: descuentoId
                                       Model model) {
        Ruta ruta = rutaService.obtenerTodasLasRutas().stream()
                .filter(r -> r.getId().equals(rutaId))
                .findFirst()
                .orElse(null);

        BigDecimal tarifaBase = (ruta != null && ruta.getTarifas() != null && !ruta.getTarifas().isEmpty())
                ? ruta.getTarifas().get(0).getPrecio()
                : null;

        BigDecimal tarifaConDescuento = tarifaBase; // Inicializar con tarifa base

        // Aplicar descuento si se seleccionó uno
        if (descuentoId != null && tarifaBase != null) {
            Optional<Descuento> descuentoOpt = descuentoService.obtenerDescuentoPorId(descuentoId);
            if (descuentoOpt.isPresent()) {
                tarifaConDescuento = descuentoService.aplicarDescuento(tarifaBase, descuentoOpt.get().getPorcentaje());
            }
        }


        List<String> ciudades = rutaService.obtenerTodasLasRutas().stream()
                .map(Ruta::getCiudad)
                .distinct()
                .collect(Collectors.toList());
        List<Ruta> rutas = (ruta != null) ? rutaService.obtenerTodasLasRutas().stream()
                .filter(r -> r.getCiudad().equals(ruta.getCiudad()))
                .collect(Collectors.toList()) : null;

        model.addAttribute("ciudades", ciudades);
        model.addAttribute("rutas", rutas);
        model.addAttribute("tarifa", tarifaBase); // Tarifa original
        model.addAttribute("tarifaConDescuento", tarifaConDescuento); // Tarifa con descuento
        model.addAttribute("cambio", model.getAttribute("cambio"));
        model.addAttribute("rutaSeleccionadaId", rutaId);
        model.addAttribute("ciudadSeleccionada", ciudadSeleccionada);
        model.addAttribute("descuentoSeleccionadoId", descuentoId); // Mantener el descuento seleccionado
        model.addAttribute("descuentos", descuentoService.obtenerTodosLosDescuentos()); // Recargar descuentos
        return "usuario/venta";
    }

    @PostMapping("/usuario/calcular-cambio")
    public String calcularCambio(@RequestParam("rutaSeleccionadaId") Long rutaId,
                                 @RequestParam("montoRecibido") BigDecimal montoRecibido,
                                 @RequestParam("ciudadSeleccionada") String ciudadSeleccionada,
                                 @RequestParam(value = "descuentoSeleccionadoId", required = false) Long descuentoId, // Nuevo: descuentoId
                                 Model model) {
        Ruta ruta = rutaService.obtenerTodasLasRutas().stream()
                .filter(r -> r.getId().equals(rutaId))
                .findFirst()
                .orElse(null);

        BigDecimal tarifaBase = (ruta != null && ruta.getTarifas() != null && !ruta.getTarifas().isEmpty())
                ? ruta.getTarifas().get(0).getPrecio()
                : BigDecimal.ZERO;

        BigDecimal tarifaFinal = tarifaBase; // Inicializar con tarifa base

        // Aplicar descuento si se seleccionó uno
        if (descuentoId != null && tarifaBase != null) {
            Optional<Descuento> descuentoOpt = descuentoService.obtenerDescuentoPorId(descuentoId);
            if (descuentoOpt.isPresent()) {
                tarifaFinal = descuentoService.aplicarDescuento(tarifaBase, descuentoOpt.get().getPorcentaje());
            }
        }

        BigDecimal cambio = montoRecibido.subtract(tarifaFinal); // Calcular cambio con tarifa final (con descuento)

        List<String> ciudades = rutaService.obtenerTodasLasRutas().stream()
                .map(Ruta::getCiudad)
                .distinct()
                .collect(Collectors.toList());
        List<Ruta> rutas = (ruta != null) ? rutaService.obtenerTodasLasRutas().stream()
                .filter(r -> r.getCiudad().equals(ruta.getCiudad()))
                .collect(Collectors.toList()) : null;

        model.addAttribute("ciudades", ciudades);
        model.addAttribute("rutas", rutas);
        model.addAttribute("tarifa", tarifaBase); // Mostrar tarifa original
        model.addAttribute("tarifaConDescuento", tarifaFinal); // Mostrar tarifa final (con descuento)
        model.addAttribute("cambio", cambio);
        model.addAttribute("rutaSeleccionadaId", rutaId);
        model.addAttribute("ciudadSeleccionada", ciudadSeleccionada);
        model.addAttribute("montoRecibido", montoRecibido);
        model.addAttribute("descuentoSeleccionadoId", descuentoId); // Mantener el descuento seleccionado
        model.addAttribute("descuentos", descuentoService.obtenerTodosLosDescuentos()); // Recargar descuentos
        return "usuario/venta";
    }

    @PostMapping("/usuario/registrar-venta")
    public String registrarVenta(@RequestParam("rutaSeleccionadaId") Long rutaId,
                                 @RequestParam("montoRecibido") BigDecimal montoRecibido,
                                 @RequestParam("ciudadSeleccionada") String ciudadSeleccionada,
                                 @RequestParam(value = "descuentoSeleccionadoId", required = false) Long descuentoId, // Nuevo: descuentoId
                                 RedirectAttributes redirectAttributes) {
        try {
            // Pasar el ID del descuento al servicio de venta
            ventaService.registrarVenta(rutaId, montoRecibido, descuentoId);
            redirectAttributes.addFlashAttribute("mensajeExitoVenta", "Venta registrada exitosamente.");
            return "redirect:/usuario/venta?ciudadSeleccionada=" + ciudadSeleccionada + "&rutaSeleccionadaId=" + rutaId + "&descuentoSeleccionadoId=" + (descuentoId != null ? descuentoId : "");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorVenta", e.getMessage());
            return "redirect:/usuario/venta?ciudadSeleccionada=" + ciudadSeleccionada + "&rutaSeleccionadaId=" + rutaId + "&descuentoSeleccionadoId=" + (descuentoId != null ? descuentoId : "");
        }
    }
}