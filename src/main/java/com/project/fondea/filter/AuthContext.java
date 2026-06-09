package com.project.fondea.filter;

import com.project.fondea.exception.UnauthorizedActionException;
import com.project.fondea.model.User;
import com.project.fondea.model.enums.Role;
import com.project.fondea.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AuthContext {

    private final UserRepository userRepository;

    public User getCurrentUser() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UnauthorizedActionException("No estás autenticado");
        }

        var principal = authentication.getPrincipal();
        System.out.println("PRINCIPAL TYPE: " + principal.getClass().getName());
        System.out.println("PRINCIPAL VALUE: " + principal);

        // DevTools puede causar conflictos de classloader en desarrollo
        // forzamos la recarga del usuario desde la BD por su id
        if (principal instanceof User user) {
            return user;
        }

        // Si el principal es String intenta parsearlo como UUID
        if (principal instanceof String principalStr) {
            try {
                UUID userId = UUID.fromString(principalStr);
                return userRepository.findById(userId)
                        .orElseThrow(() -> new UnauthorizedActionException("Usuario no encontrado"));
            } catch (IllegalArgumentException e) {
                throw new UnauthorizedActionException("No se pudo obtener el usuario autenticado");
            }
        }

        throw new UnauthorizedActionException("No se pudo obtener el usuario autenticado");
    }

    public UUID getCurrentUserId() {
        return getCurrentUser().getId();
    }

    public Role getCurrentUserRole() {
        return getCurrentUser().getRole();
    }
}
