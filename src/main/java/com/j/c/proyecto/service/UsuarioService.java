package com.j.c.proyecto.service;

import com.j.c.proyecto.exception.UsuarioEnUsoException;
import com.j.c.proyecto.exception.UsuarioNoEncontradoException;
import com.j.c.proyecto.model.Usuario;
import com.j.c.proyecto.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public List<Usuario> obtenerTodosLosUsuarios() {
        return usuarioRepository.findAll();
    }

    @Transactional
    public void eliminarUsuario(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNoEncontradoException(id));

        if (usuarioTieneRegistrosAsociados(usuario)) {
            throw new UsuarioEnUsoException(id);
        }

        usuarioRepository.delete(usuario);
    }

    private boolean usuarioTieneRegistrosAsociados(Usuario usuario) {
        // Implementa la lógica para verificar si el usuario tiene registros asociados
        // Por ejemplo, verificar si hay ventas, reservas, etc. asociadas al usuario
        // return ventaRepository.existsByUsuario(usuario) || reservaRepository.existsByUsuario(usuario);
        return false; // Cambiar por implementación real
    }
}