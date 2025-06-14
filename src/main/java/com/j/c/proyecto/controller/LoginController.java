package com.j.c.proyecto.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String mostrarFormularioLogin(@RequestParam(value = "error", required = false) String error,
                                         Model model) {
        if (error != null) {
            model.addAttribute("error", "Nombre de usuario o contraseña incorrectos");
        }
        return "login";
    }
}