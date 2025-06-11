package com.j.c.proyecto.service;

import com.j.c.proyecto.dto.RegistroDTO;
import com.j.c.proyecto.exception.EmailExistenteException;
import com.j.c.proyecto.exception.PasswordInvalidoException;
import com.j.c.proyecto.exception.UsuarioExistenteException;
import com.j.c.proyecto.model.Rol;
import com.j.c.proyecto.model.Usuario;
import com.j.c.proyecto.repository.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Registra un nuevo usuario en el sistema
     * @param registroDTO Datos para el registro del usuario
     * @throws ResponseStatusException Si el username/email existen o la contraseña es inválida
     */
    public void registrarUsuario(RegistroDTO registroDTO) {
        if (usuarioRepository.existsByUsername(registroDTO.getUsername())) {
            throw new UsuarioExistenteException(registroDTO.getUsername());
        }

        if (usuarioRepository.existsByEmail(registroDTO.getEmail())) {
            throw new EmailExistenteException(registroDTO.getEmail());
        }

        if (registroDTO.getPassword() == null || registroDTO.getPassword().length() < 8) {
            throw new PasswordInvalidoException();
        }

        // Creación de usuario
        Usuario usuario = new Usuario();
        usuario.setUsername(registroDTO.getUsername());
        usuario.setEmail(registroDTO.getEmail());
        usuario.setPassword(passwordEncoder.encode(registroDTO.getPassword()));
        usuario.setRol(Rol.USUARIO);

        usuarioRepository.save(usuario);
    }
}