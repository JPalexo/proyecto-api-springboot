package com.j.c.proyecto.controller;

import com.j.c.proyecto.model.Ruta;
import com.j.c.proyecto.service.RutaService;
import com.j.c.proyecto.service.VentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class UsuarioController {

    private final RutaService rutaService;
    private final VentaService ventaService;

    @Autowired
    public UsuarioController(RutaService rutaService, VentaService ventaService) {
        this.rutaService = rutaService;
        this.ventaService = ventaService;
    }

    @GetMapping("/usuario/venta")
    public String mostrarPaginaVenta(Model model, @RequestParam(value = "ciudadSeleccionada", required = false) String ciudadSeleccionada,
                                     @RequestParam(value = "rutaSeleccionadaId", required = false) Long rutaSeleccionadaId) {
        List<String> ciudades = rutaService.obtenerTodasLasRutas().stream()
                .map(Ruta::getCiudad)
                .distinct()
                .collect(Collectors.toList());
        model.addAttribute("ciudades", ciudades);
        model.addAttribute("ciudadSeleccionada", ciudadSeleccionada);
        model.addAttribute("rutaSeleccionadaId", rutaSeleccionadaId);
        List<Ruta> rutas = null;
        if (ciudadSeleccionada != null) {
            rutas = rutaService.obtenerTodasLasRutas().stream()
                    .filter(ruta -> ruta.getCiudad().equals(ciudadSeleccionada))
                    .collect(Collectors.toList());
        }
        model.addAttribute("rutas", rutas);
        model.addAttribute("tarifa", null);
        model.addAttribute("cambio", null);
        return "usuario/venta";
    }

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
        model.addAttribute("ciudadSeleccionada", ciudad); // Pasar la ciudad seleccionada
        model.addAttribute("rutaSeleccionadaId", null); // Resetear la ruta al cambiar la ciudad
        model.addAttribute("tarifa", null);
        model.addAttribute("cambio", null);
        return "usuario/venta";
    }

    @PostMapping("/usuario/obtener-tarifa")
    public String obtenerTarifaPorRuta(@RequestParam("rutaSeleccionada") Long rutaId,
                                       @RequestParam("ciudadSeleccionada") String ciudadSeleccionada, // Recibir la ciudad seleccionada
                                       Model model) {
        Ruta ruta = rutaService.obtenerTodasLasRutas().stream()
                .filter(r -> r.getId().equals(rutaId))
                .findFirst()
                .orElse(null);
        BigDecimal tarifa = (ruta != null && ruta.getTarifas() != null && !ruta.getTarifas().isEmpty())
                ? ruta.getTarifas().get(0).getPrecio()
                : null;

        List<String> ciudades = rutaService.obtenerTodasLasRutas().stream()
                .map(Ruta::getCiudad)
                .distinct()
                .collect(Collectors.toList());
        List<Ruta> rutas = (ruta != null) ? rutaService.obtenerTodasLasRutas().stream()
                .filter(r -> r.getCiudad().equals(ruta.getCiudad()))
                .collect(Collectors.toList()) : null;

        model.addAttribute("ciudades", ciudades);
        model.addAttribute("rutas", rutas);
        model.addAttribute("tarifa", tarifa);
        model.addAttribute("cambio", model.getAttribute("cambio"));
        model.addAttribute("rutaSeleccionadaId", rutaId);
        model.addAttribute("ciudadSeleccionada", ciudadSeleccionada); // Pasar la ciudad seleccionada
        return "usuario/venta";
    }

    @PostMapping("/usuario/calcular-cambio")
    public String calcularCambio(@RequestParam("rutaSeleccionadaId") Long rutaId,
                                 @RequestParam("montoRecibido") BigDecimal montoRecibido,
                                 @RequestParam("ciudadSeleccionada") String ciudadSeleccionada, // Recibir la ciudad seleccionada
                                 Model model) {
        Ruta ruta = rutaService.obtenerTodasLasRutas().stream()
                .filter(r -> r.getId().equals(rutaId))
                .findFirst()
                .orElse(null);
        BigDecimal tarifa = (ruta != null && ruta.getTarifas() != null && !ruta.getTarifas().isEmpty())
                ? ruta.getTarifas().get(0).getPrecio()
                : BigDecimal.ZERO;
        BigDecimal cambio = montoRecibido.subtract(tarifa);

        List<String> ciudades = rutaService.obtenerTodasLasRutas().stream()
                .map(Ruta::getCiudad)
                .distinct()
                .collect(Collectors.toList());
        List<Ruta> rutas = (ruta != null) ? rutaService.obtenerTodasLasRutas().stream()
                .filter(r -> r.getCiudad().equals(ruta.getCiudad()))
                .collect(Collectors.toList()) : null;

        model.addAttribute("ciudades", ciudades);
        model.addAttribute("rutas", rutas);
        model.addAttribute("tarifa", tarifa);
        model.addAttribute("cambio", cambio);
        model.addAttribute("rutaSeleccionadaId", rutaId);
        model.addAttribute("ciudadSeleccionada", ciudadSeleccionada); // Pasar la ciudad seleccionada
        model.addAttribute("montoRecibido", montoRecibido);
        return "usuario/venta";
    }

    @PostMapping("/usuario/registrar-venta")
    public String registrarVenta(@RequestParam("rutaSeleccionadaId") Long rutaId,
                                 @RequestParam("montoRecibido") BigDecimal montoRecibido,
                                 @RequestParam("ciudadSeleccionada") String ciudadSeleccionada, // Recibir la ciudad seleccionada
                                 RedirectAttributes redirectAttributes) {
        try {
            ventaService.registrarVenta(rutaId, montoRecibido);
            redirectAttributes.addFlashAttribute("mensajeExitoVenta", "Venta registrada exitosamente.");
            return "redirect:/usuario/venta?ciudadSeleccionada=" + ciudadSeleccionada + "&rutaSeleccionadaId=" + rutaId;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorVenta", e.getMessage());
            return "redirect:/usuario/venta?ciudadSeleccionada=" + ciudadSeleccionada + "&rutaSeleccionadaId=" + rutaId;
        }
    }
}