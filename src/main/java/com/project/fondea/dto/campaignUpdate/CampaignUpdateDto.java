package com.project.fondea.dto.campaignUpdate;

import com.project.fondea.model.enums.CampaignUpdateVisibility;

import java.time.LocalDateTime;
import java.util.UUID;

public record CampaignUpdateDto(
        UUID id,
        String title,
        String body,
        LocalDateTime createdAt,
        CampaignUpdateVisibility visibility
) {
}
