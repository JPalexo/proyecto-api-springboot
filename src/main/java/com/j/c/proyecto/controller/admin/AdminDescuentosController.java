package com.j.c.proyecto.controller.admin;

import com.j.c.proyecto.dto.DescuentoDTO;
import com.j.c.proyecto.exception.DescuentoExistenteException;
import com.j.c.proyecto.exception.DescuentoNoEncontradoException;
import com.j.c.proyecto.service.DescuentoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/admin/descuentos") // Prefijo para las URLs de descuentos administrativos
public class AdminDescuentosController {

    private final DescuentoService descuentoService;

    @Autowired
    public AdminDescuentosController(DescuentoService descuentoService) {
        this.descuentoService = descuentoService;
    }

    @GetMapping
    public String listarDescuentos(Model model) {
        // Carga todos los descuentos para la lista
        model.addAttribute("descuentos", descuentoService.obtenerTodosLosDescuentos());

        // Prepara un nuevo DTO para el formulario de agregar/editar, si no hay uno ya en el modelo
        if (!model.containsAttribute("descuentoDTO")) {
            model.addAttribute("descuentoDTO", new DescuentoDTO());
        }
        return "admin/descuentos"; // El nombre de tu vista Thymeleaf para gestionar descuentos
    }

    @PostMapping("/guardar")
    public String guardarDescuento(@Valid @ModelAttribute("descuentoDTO") DescuentoDTO descuentoDTO,
                                   BindingResult result,
                                   RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            // Si hay errores de validación, se añade el DTO con los errores y se redirige
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.descuentoDTO", result);
            redirectAttributes.addFlashAttribute("descuentoDTO", descuentoDTO);
            redirectAttributes.addFlashAttribute("errorGuardar", "Error de validación. Verifica los datos del descuento.");
            return "redirect:/admin/descuentos"; // Redirige a la misma página para mostrar errores
        }

        try {
            descuentoService.guardarDescuento(descuentoDTO);
            redirectAttributes.addFlashAttribute("mensajeExito", "Descuento guardado exitosamente.");
        } catch (DescuentoExistenteException e) {
            redirectAttributes.addFlashAttribute("errorGuardar", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorGuardar", "Error al guardar el descuento: " + e.getMessage());
        }
        return "redirect:/admin/descuentos";
    }

    @GetMapping("/editar/{id}")
    public String editarDescuento(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        // Busca el descuento por ID y si lo encuentra, lo precarga en el formulario
        Optional<DescuentoDTO> descuentoOpt = descuentoService.obtenerDescuentoPorId(id)
                .map(descuento -> {
                    DescuentoDTO dto = new DescuentoDTO();
                    dto.setId(descuento.getId());
                    dto.setNombre(descuento.getNombre());
                    dto.setPorcentaje(descuento.getPorcentaje());
                    return dto;
                });

        if (descuentoOpt.isPresent()) {
            model.addAttribute("descuentoDTO", descuentoOpt.get());
        } else {
            redirectAttributes.addFlashAttribute("errorEditar", "Descuento con ID " + id + " no encontrado.");
            return "redirect:/admin/descuentos";
        }

        // Carga la lista completa de descuentos para la tabla en la misma vista
        model.addAttribute("descuentos", descuentoService.obtenerTodosLosDescuentos());
        return "admin/descuentos"; // Reutiliza la misma vista para listar y editar
    }

    @PostMapping("/actualizar/{id}")
    public String actualizarDescuento(@PathVariable("id") Long id,
                                      @Valid @ModelAttribute("descuentoDTO") DescuentoDTO descuentoDTO,
                                      BindingResult result,
                                      RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.descuentoDTO", result);
            redirectAttributes.addFlashAttribute("descuentoDTO", descuentoDTO);
            redirectAttributes.addFlashAttribute("errorActualizar", "Error de validación al actualizar.");
            return "redirect:/admin/descuentos";
        }

        try {
            descuentoService.actualizarDescuento(id, descuentoDTO);
            redirectAttributes.addFlashAttribute("mensajeExito", "Descuento actualizado exitosamente.");
        } catch (DescuentoNoEncontradoException | DescuentoExistenteException e) {
            redirectAttributes.addFlashAttribute("errorActualizar", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorActualizar", "Error al actualizar el descuento: " + e.getMessage());
        }
        return "redirect:/admin/descuentos";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminarDescuento(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            descuentoService.eliminarDescuento(id);
            redirectAttributes.addFlashAttribute("mensajeExito", "Descuento eliminado exitosamente.");
        } catch (DescuentoNoEncontradoException e) {
            redirectAttributes.addFlashAttribute("errorEliminar", e.getMessage());
        } catch (Exception e) {
            // Manejar otras posibles excepciones, como DataIntegrityViolationException
            // si el descuento estuviera en uso (aunque actualmente Venta no tiene FK a Descuento)
            redirectAttributes.addFlashAttribute("errorEliminar", "Error al eliminar el descuento: " + e.getMessage());
        }
        return "redirect:/admin/descuentos";
    }
}