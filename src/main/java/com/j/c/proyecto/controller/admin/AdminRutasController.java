package com.j.c.proyecto.controller.admin;

import com.j.c.proyecto.dto.RutaDTO;
import com.j.c.proyecto.model.Ruta;
import com.j.c.proyecto.service.RutaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/rutas")
public class AdminRutasController {

    private final RutaService rutaService;

    @Autowired
    public AdminRutasController(RutaService rutaService) {
        this.rutaService = rutaService;
    }

    @GetMapping
    public String mostrarFormularioRutas(Model model) {
        List<Ruta> rutas = rutaService.obtenerTodasLasRutas();
        model.addAttribute("rutas", rutas);
        if (!model.containsAttribute("rutaDTO")) {
            model.addAttribute("rutaDTO", new RutaDTO());
        }
        return "admin/rutas";
    }

    @PostMapping
    public String registrarNuevaRuta(@Valid @ModelAttribute("rutaDTO") RutaDTO rutaDTO,
                                     BindingResult result,
                                     RedirectAttributes redirectAttributes,
                                     Model model) {
        if (result.hasErrors()) {
            List<Ruta> rutas = rutaService.obtenerTodasLasRutas();
            model.addAttribute("rutas", rutas);
            return "admin/rutas";
        }

        try {
            rutaService.guardarRuta(rutaDTO);
            redirectAttributes.addFlashAttribute("mensajeExito", "Ruta registrada exitosamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorGuardado", e.getMessage());
        }
        return "redirect:/admin/rutas";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminarRuta(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            rutaService.eliminarRuta(id);
            redirectAttributes.addFlashAttribute("mensajeExito", "Ruta eliminada exitosamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorEliminar", e.getMessage());
        }
        return "redirect:/admin/rutas";
    }
}