package com.project.fondea.filter;

import com.project.fondea.model.User;
import com.project.fondea.model.enums.Role;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.UUID;

@Component
public class AuthContext {
    public User getCurrentUser() {
        return (User) Objects.requireNonNull(SecurityContextHolder.getContext()
                        .getAuthentication())
                .getPrincipal();
    }

    public UUID getCurrentUserId() {
        return getCurrentUser().getId();
    }

    public Role getCurrentUserRole() {
        return getCurrentUser().getRole();
    }
}
