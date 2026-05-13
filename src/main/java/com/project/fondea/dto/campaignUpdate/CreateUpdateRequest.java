package com.project.fondea.dto.campaignUpdate;

import jakarta.validation.constraints.NotBlank;

public record CreateUpdateRequest(
        @NotBlank(message = "El titulo es requerido")
        String title,

        @NotBlank(message = "El contenido es requerido")
        String body
) {
}
