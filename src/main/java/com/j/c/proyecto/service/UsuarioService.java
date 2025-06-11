package com.j.c.proyecto.service;

import com.j.c.proyecto.model.Ruta;
import com.j.c.proyecto.model.Usuario;
import com.j.c.proyecto.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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
    public void eliminarUsuario(Long id) throws Exception {
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(id);
        if (usuarioOptional.isPresent()) {
            // Aquí podrías agregar lógica para verificar si hay un usuario
            // antes de eliminarlo, y lanzar una excepción si es así.
            usuarioRepository.deleteById(id);
        } else {
            throw new Exception("No se encontró el usuario con el ID: " + id);
        }
    }
}