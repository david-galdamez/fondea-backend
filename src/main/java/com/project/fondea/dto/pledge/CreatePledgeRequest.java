package com.project.fondea.dto.pledge;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.UUID;

public record CreatePledgeRequest(
        @NotNull(message = "La campaña es requerida")
        UUID campaignId,

        UUID rewardId,

        @NotNull(message = "El monto es requerido")
        @Positive(message = "El monto debe ser mayor a 0")
        BigDecimal amount
) {
}
