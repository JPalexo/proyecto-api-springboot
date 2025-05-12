package com.j.c.proyecto.controller;

import com.j.c.proyecto.dto.RegistroDTO;
import com.j.c.proyecto.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
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