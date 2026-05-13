package com.project.fondea.dto.rewards;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateRewardRequest(

        @NotBlank(message = "El título es requerido")
        String title,

        String description,

        @NotNull(message = "El monto mínimo es requerido")
        @Positive(message = "El monto mínimo debe ser mayor a 0")
        BigDecimal minAmount,

        // null = ilimitado, número = stock limitado
        @Min(value = 1, message = "El stock debe ser al menos 1")
        Integer stock,

        LocalDate estimatedDelivery
) {}
