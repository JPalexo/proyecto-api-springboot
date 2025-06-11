package com.j.c.proyecto.controller.admin;

import com.j.c.proyecto.dto.TarifaDTO;
import com.j.c.proyecto.model.Ruta;
import com.j.c.proyecto.model.Tarifa;
import com.j.c.proyecto.service.RutaService;
import com.j.c.proyecto.service.TarifaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/tarifas")
public class AdminTarifasController {

    private final TarifaService tarifaService;
    private final RutaService rutaService;

    @Autowired
    public AdminTarifasController(TarifaService tarifaService, RutaService rutaService) {
        this.tarifaService = tarifaService;
        this.rutaService = rutaService;
    }

    @GetMapping
    public String mostrarFormularioTarifas(Model model) {
        List<Tarifa> tarifas = tarifaService.obtenerTodasLasTarifasOrdenadasPorRuta();
        System.out.println("Número de tarifas encontradas: " + tarifas.size());
        List<Ruta> rutasSinTarifa = rutaService.obtenerTodasLasRutas().stream()
                .filter(ruta -> tarifaService.obtenerTodasLasTarifasOrdenadasPorRuta().stream()
                        .noneMatch(tarifa -> tarifa.getRuta().getId().equals(ruta.getId())))
                .toList();

        model.addAttribute("tarifas", tarifas);
        model.addAttribute("rutasSinTarifa", rutasSinTarifa);
        if (!model.containsAttribute("tarifaDTO")) {
            model.addAttribute("tarifaDTO", new TarifaDTO());
        }
        return "admin/tarifas";
    }

    @PostMapping("/guardar")
    public String guardarTarifa(@Valid @ModelAttribute("tarifaDTO") TarifaDTO tarifaDTO,
                                BindingResult result,
                                RedirectAttributes redirectAttributes,
                                Model model) {
        if (result.hasErrors()) {
            List<Tarifa> tarifas = tarifaService.obtenerTodasLasTarifasOrdenadasPorRuta();
            List<Ruta> rutasSinTarifa = rutaService.obtenerTodasLasRutas().stream()
                    .filter(ruta -> tarifaService.obtenerTodasLasTarifasOrdenadasPorRuta().stream()
                            .noneMatch(tarifa -> tarifa.getRuta().getId().equals(ruta.getId())))
                    .toList();
            model.addAttribute("tarifas", tarifas);
            model.addAttribute("rutasSinTarifa", rutasSinTarifa);
            return "admin/tarifas";
        }

        try {
            tarifaService.guardarTarifa(tarifaDTO);
            redirectAttributes.addFlashAttribute("mensajeExito", "Tarifa configurada exitosamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorGuardado", e.getMessage());
        }
        return "redirect:/admin/tarifas";
    }

    @PostMapping("/actualizar/{rutaId}")
    public String actualizarTarifa(@PathVariable("rutaId") Long rutaId,
                                   @RequestParam("precio") java.math.BigDecimal precio,
                                   RedirectAttributes redirectAttributes) {
        try {
            tarifaService.actualizarTarifa(rutaId, precio);
            redirectAttributes.addFlashAttribute("mensajeExito", "Tarifa actualizada exitosamente para la ruta con ID: " + rutaId);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorActualizar", e.getMessage());
        }
        return "redirect:/admin/tarifas";
    }
}