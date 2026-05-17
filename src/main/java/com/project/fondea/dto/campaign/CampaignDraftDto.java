package com.project.fondea.dto.campaign;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record CampaignDraftDto(
        UUID id,
        String title,
        BigDecimal goalAmount,
        LocalDate deadline,
        LocalDateTime createdAt
) {
}
