package com.project.fondea.dto.campaign;

import com.project.fondea.model.enums.CampaignStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record CampaignCreatedDto(
        UUID id,
        String title,
        CampaignStatus status,       // siempre DRAFT
        LocalDateTime createdAt
) {
}
