package com.project.fondea.dto.campaign;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
public class RegisterCampaignRequest {
    @NotBlank(message = "El titlo es requerido")
    private String title;

    @NotBlank(message = "La descripción es requerido")
    private String description;

    @NotNull(message = "La meta es requerida")
    private BigDecimal goalAmount;

    @NotNull(message = "El campo es requerido")
    private Boolean isFlexibleGoal;

    @NotNull(message = "La fecha limite es requerida")
    @Future(message = "La fecha limite debe ser en el futuro")
    private LocalDate deadline;

    @NotNull(message = "El id de la categoria es requerida")
    private UUID categoryId;

    @NotNull(message = "El id de la ubicación es requerida")
    private UUID locationId;
}
