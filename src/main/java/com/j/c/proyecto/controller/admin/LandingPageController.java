package com.j.c.proyecto.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class LandingPageController {

    @GetMapping("/dashboard") // Esta será la landing page del administrador
    public String mostrarDashboardAdmin() {
        return "admin/dashboard"; // Crea esta vista (admin/dashboard.html)
    }
}