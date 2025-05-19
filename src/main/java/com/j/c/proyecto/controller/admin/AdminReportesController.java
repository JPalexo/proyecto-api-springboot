package com.j.c.proyecto.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminReportesController {

    @GetMapping("/reportes-ventas")
    public String mostrarReportesVentas(Model model) {
        // Puedes agregar datos al modelo si es necesario
        return "admin/reportes-ventas";
    }

    // Otros métodos del controlador para el admin...
}