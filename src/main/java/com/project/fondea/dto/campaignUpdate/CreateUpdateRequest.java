package com.project.fondea.dto.campaignUpdate;

import com.project.fondea.model.enums.CampaignUpdateVisibility;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateUpdateRequest(
        @NotBlank(message = "El titulo es requerido")
        String title,

        @NotBlank(message = "El contenido es requerido")
        String body,

        @NotNull(message = "La visibilidad es obligatoria")
        CampaignUpdateVisibility visibility
) {
}
