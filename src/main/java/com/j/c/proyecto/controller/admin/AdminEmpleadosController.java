package com.j.c.proyecto.controller.admin;

import com.j.c.proyecto.model.Usuario;
import com.j.c.proyecto.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes; // Importar RedirectAttributes

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

    // Nuevo metodo para eliminar empleado
    @PostMapping("/eliminar/{id}")
    public String eliminarEmpleado(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            usuarioService.eliminarUsuario(id);
            redirectAttributes.addFlashAttribute("mensajeExito", "Empleado eliminado exitosamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeError", "Error al eliminar empleado: " + e.getMessage());
        }
        return "redirect:/admin/administrar-empleados"; // Redirige de vuelta a la página de administración
    }
}