package com.j.c.proyecto.service;

import com.j.c.proyecto.dto.RegistroDTO;
import com.j.c.proyecto.model.Rol;
import com.j.c.proyecto.model.Usuario;
import com.j.c.proyecto.repository.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void registrarUsuario(RegistroDTO registroDTO) {
        if (usuarioRepository.existsByUsername(registroDTO.getUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El nombre de usuario ya existe");
        }
        if (usuarioRepository.existsByEmail(registroDTO.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El correo electrónico ya existe");
        }

        Usuario usuario = new Usuario();
        usuario.setUsername(registroDTO.getUsername());
        usuario.setEmail(registroDTO.getEmail());
        usuario.setPassword(passwordEncoder.encode(registroDTO.getPassword()));
        usuario.setRol(Rol.USUARIO); // Por defecto, asignamos el rol de USUARIO al registrarse
        usuarioRepository.save(usuario);
    }

    // La lógica de inicio de sesión será manejada por Spring Security
}