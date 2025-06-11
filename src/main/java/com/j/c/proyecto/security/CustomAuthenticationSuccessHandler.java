package com.j.c.proyecto.security;

import com.j.c.proyecto.exception.AuthenticationRedirectException;
import com.j.c.proyecto.exception.RolInvalidoException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Set;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        @NotNull Authentication authentication) {
        try {
            Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());

            if (roles.contains("ADMIN")) {
                response.sendRedirect("/admin/dashboard");
            } else if (roles.contains("USUARIO")) {
                response.sendRedirect("/usuario/venta");
            } else {
                throw new RolInvalidoException(roles.toString());
            }
        } catch (IOException e) {
            throw new AuthenticationRedirectException("Error en redirección", e);
        }
    }
}