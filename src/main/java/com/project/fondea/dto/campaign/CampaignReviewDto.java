package com.project.fondea.dto.campaign;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record CampaignReviewDto(
        UUID id,
        String title,
        String description,
        UUID creatorId,
        String creatorName,
        String creatorEmail,         // para contactar al creador si hay dudas
        BigDecimal goalAmount,
        Boolean isFlexibleGoal,
        LocalDate deadline,
        String categoryName,
        String locationCity,
        LocalDateTime submittedAt    // cuándo lo envió a revisión
) {
}
