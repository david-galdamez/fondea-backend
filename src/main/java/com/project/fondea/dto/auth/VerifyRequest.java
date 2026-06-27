package com.project.fondea.dto.auth;

import jakarta.validation.constraints.NotBlank;

public record VerifyRequest(
        @NotBlank(message = "El código es requerido")
        String code
) {}