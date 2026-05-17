package com.project.fondea.dto.campaign;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record RegisterCampaignRequest (
    @NotBlank(message = "El titlo es necesario")
    String title,

    @NotBlank(message = "La descripción es necesario")
    String description,

    BigDecimal goalAmount,

    @NotNull(message = "El campo es necesario")
    Boolean isFlexibleGoal,

    @NotNull(message = "La fecha limite es necesaria")
    @Future(message = "La fecha limite debe ser en el futuro")
    LocalDate deadline,

    @NotNull(message = "El id de la categoria es necesario")
    UUID categoryId,

    @NotNull(message = "El id de la ubicación es necesario")
    UUID locationId
){}
