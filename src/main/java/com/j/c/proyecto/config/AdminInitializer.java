package com.j.c.proyecto.config;

import com.j.c.proyecto.model.Rol;
import com.j.c.proyecto.model.Usuario;
import com.j.c.proyecto.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AdminInitializer {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminInitializer(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public CommandLineRunner initializeAdminUser() {
        return args -> {
            if (!usuarioRepository.existsByUsername("admin")) {
                Usuario adminUser = new Usuario();
                adminUser.setUsername("admin");
                adminUser.setPassword(passwordEncoder.encode("00000000"));
                adminUser.setEmail("admin@correo.com");
                adminUser.setRol(Rol.ADMIN);
                usuarioRepository.save(adminUser);
                System.out.println("Usuario administrador inicial creado.");
            } else {
                System.out.println("El usuario administrador ya existe.");
            }
        };
    }
}