package com.project.fondea.dto.campaignUpdate;

import java.time.LocalDateTime;
import java.util.UUID;

public record CampaignUpdateDto(
        UUID id,
        String title,
        String body,
        LocalDateTime createdAt
) {
}
