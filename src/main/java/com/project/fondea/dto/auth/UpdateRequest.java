package com.project.fondea.dto.auth;

import jakarta.validation.constraints.NotBlank;

public record UpdateRequest(
        @NotBlank(message = "El nombre es requerido")
        String name,

        String city,

        String country,

        String bio

) {
}
