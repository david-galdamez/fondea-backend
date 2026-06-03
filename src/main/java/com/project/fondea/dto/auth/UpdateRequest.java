package com.project.fondea.dto.auth;

import jakarta.validation.constraints.NotBlank;

public record UpdateRequest(
        @NotBlank(message = "El nombre es requerido")
        String name,

        @NotBlank(message = "La ciudad es requerida")
        String city,

        @NotBlank(message = "El pais es requerido")
        String country,

        String bio

) {
}
