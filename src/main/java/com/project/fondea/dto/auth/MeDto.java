package com.project.fondea.dto.auth;

import java.time.LocalDateTime;
import java.util.UUID;

public record MeDto(
        UUID id,
        String name,
        String email,
        String city,
        String country,
        String bio,
        String role,
        Boolean isVerified,
        LocalDateTime createdAt
) {}