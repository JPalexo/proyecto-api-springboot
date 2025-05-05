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
    public String mostrarFormularioRegistro(Model model) {
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
            return "redirect:/login?registroExitoso"; // Redirige a la página de inicio de sesión con un mensaje de éxito
        } catch (Exception e) {
            model.addAttribute("errorRegistro", e.getMessage());
            return "registro"; // Vuelve a mostrar el formulario con un mensaje de error
        }
    }

    @GetMapping("/login")
    public String mostrarFormularioLogin(@RequestParam(value = "error", required = false) String error,
                                         @RequestParam(value = "logout", required = false) String logout,
                                         @RequestParam(value = "registroExitoso", required = false) String registroExitoso,
                                         Model model) {
        if (error != null) {
            model.addAttribute("error", "Nombre de usuario o contraseña incorrectos");
        }
        if (logout != null) {
            model.addAttribute("logout", "Has cerrado sesión exitosamente");
        }
        if (registroExitoso != null) {
            model.addAttribute("registroExitoso", "Te has registrado exitosamente. ¡Inicia sesión!");
        }
        return "login";
    }
}