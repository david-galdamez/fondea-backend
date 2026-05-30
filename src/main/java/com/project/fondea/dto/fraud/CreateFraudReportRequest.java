package com.project.fondea.dto.fraud;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateFraudReportRequest(

        @NotNull(message = "La campaña es requerida")
        UUID campaignId,

        @NotBlank(message = "La razón del reporte es requerida")
        String reason

) {
}