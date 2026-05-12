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
    @NotBlank(message = "El titlo es necesario")
    private String title;

    @NotBlank(message = "La descripción es necesario")
    private String description;

    private BigDecimal goalAmount;

    @NotNull(message = "El campo es necesario")
    private Boolean isFlexibleGoal;

    @NotNull(message = "La fecha limite es necesaria")
    @Future(message = "La fecha limite debe ser en el futuro")
    private LocalDate deadline;

    @NotNull(message = "El id de la categoria es necesario")
    private UUID categoryId;

    @NotNull(message = "El id de la ubicación es necesario")
    private UUID locationId;
}
