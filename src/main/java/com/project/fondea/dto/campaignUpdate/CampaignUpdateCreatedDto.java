package com.project.fondea.dto.campaignUpdate;

import java.time.LocalDateTime;
import java.util.UUID;

public record CampaignUpdateCreatedDto(
        UUID id,
        String title,
        LocalDateTime createdAt,
        boolean notificationSent
) {
}
