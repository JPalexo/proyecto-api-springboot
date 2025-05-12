package com.j.c.proyecto.controller.admin;

import com.j.c.proyecto.model.Usuario;
import com.j.c.proyecto.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminEmpleadosController {

    private final UsuarioService usuarioService;

    @Autowired
    public AdminEmpleadosController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/administrar-empleados")
    public String administrarEmpleados(Model model) {
        List<Usuario> usuarios = usuarioService.obtenerTodosLosUsuarios();
        model.addAttribute("usuarios", usuarios); // Pasa la lista de usuarios al modelo
        return "admin/administrar-empleados"; // Retorna la vista
    }
}