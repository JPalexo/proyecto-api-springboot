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
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/registro")
    public String mostrarFormularioRegistro(@RequestParam(value = "registroExitoso", required = false) String registroExitoso,
                                            Model model) {
        if (registroExitoso != null) {
            model.addAttribute("registroExitoso", "Te has registrado exitosamente.");
        }
        model.addAttribute("usuario", new RegistroDTO());
        return "registro";
    }

    @PostMapping("/registro")
    public String registrarUsuario(@Valid @ModelAttribute("usuario") RegistroDTO registroDTO, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "registro"; // Vuelve a mostrar el formulario con errores
        }
        try {
            authService.registrarUsuario(registroDTO);
            return "redirect:/registro?registroExitoso"; // manda un mensaje de éxito
        } catch (Exception e) {
            model.addAttribute("errorRegistro", e.getMessage());
            return "registro"; // Vuelve a mostrar el formulario con un mensaje de error
        }
    }

    @GetMapping("/login")
    public String mostrarFormularioLogin(@RequestParam(value = "error", required = false) String error,
                                         Model model) {
        if (error != null) {
            model.addAttribute("error", "Nombre de usuario o contraseña incorrectos");
        }
        return "login";
    }
}