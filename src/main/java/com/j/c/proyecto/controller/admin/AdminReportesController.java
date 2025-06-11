package com.j.c.proyecto.controller.admin;

import com.j.c.proyecto.model.Venta;
import com.j.c.proyecto.service.VentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminReportesController {

    private final VentaService ventaService;

    @Autowired
    public AdminReportesController(VentaService ventaService) {
        this.ventaService = ventaService;
    }

    @GetMapping("/reportes-ventas")
    public String mostrarFormularioReportes(Model model) {
        // Puedes inicializar algún dato necesario para el formulario
        return "admin/reportes-ventas";
    }

    @PostMapping("/generar-reporte")
    public String generarReporteVentas(
            @RequestParam String tipoReporte,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin,
            Model model) {

        LocalDateTime startDateTime;
        LocalDateTime endDateTime = LocalDateTime.now();

        // Calcular fechas según el tipo de reporte
        switch(tipoReporte) {
            case "diario":
                startDateTime = endDateTime.toLocalDate().atStartOfDay();
                break;
            case "semanal":
                startDateTime = endDateTime.minusDays(7);
                break;
            case "mensual":
                startDateTime = endDateTime.minusMonths(1);
                break;
            case "anual":
                startDateTime = endDateTime.minusYears(1);
                break;
            case "rango":
                if (fechaInicio == null || fechaFin == null) {
                    model.addAttribute("error", "Debe especificar ambas fechas para un rango personalizado");
                    return "admin/reportes-ventas";
                }
                startDateTime = fechaInicio.atStartOfDay();
                endDateTime = fechaFin.atTime(23, 59, 59);
                break;
            default:
                model.addAttribute("error", "Tipo de reporte no válido");
                return "admin/reportes-ventas";
        }

        List<Venta> reporte = ventaService.obtenerVentasPorFecha(startDateTime, endDateTime);
        model.addAttribute("reporte", reporte);
        model.addAttribute("tipoReporte", tipoReporte);
        model.addAttribute("fechaInicio", startDateTime.toLocalDate());
        model.addAttribute("fechaFin", endDateTime.toLocalDate());

        return "admin/reportes-ventas";
    }

}