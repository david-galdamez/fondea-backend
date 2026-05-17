package com.project.fondea.dto.withdrawal;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.UUID;

public record CreateWithdrawalRequest(
        @NotNull(message = "La campaña es requerida")
        UUID campaignId,

        @NotNull(message = "El monto es requerido")
        @Positive(message = "El monto debe ser mayor a 0")
        BigDecimal grossAmount
) {
}
