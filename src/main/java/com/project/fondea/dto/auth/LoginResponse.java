package com.project.fondea.dto.auth;

import com.project.fondea.model.enums.Role;

import java.time.LocalDateTime;
import java.util.UUID;

public record LoginResponse(
        String token,
        UUID id,
        String name,
        String email,
        Role role,

        LocalDateTime createdAt,
        String bio,
        String city,
        String country
) {
}
