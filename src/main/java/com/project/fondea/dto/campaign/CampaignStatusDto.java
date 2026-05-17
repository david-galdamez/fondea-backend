package com.project.fondea.dto.campaign;

import com.project.fondea.model.enums.CampaignStatus;

import java.util.UUID;

public record CampaignStatusDto(
        UUID id,
        String title,
        CampaignStatus status
) {
}
