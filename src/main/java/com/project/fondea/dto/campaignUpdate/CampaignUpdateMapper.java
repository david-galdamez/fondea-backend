package com.project.fondea.dto.campaignUpdate;

import com.project.fondea.model.CampaignUpdate;

public class CampaignUpdateMapper {
    public static CampaignUpdateCreatedDto toCreated(CampaignUpdate update) {
        return new CampaignUpdateCreatedDto(
                update.getId(),
                update.getTitle(),
                update.getCreatedAt(),
                update.getNotificationSent()
        );
    }

    public static CampaignUpdateDto toDto(CampaignUpdate update) {
        return new CampaignUpdateDto(
                update.getId(),
                update.getTitle(),
                update.getBody(),
                update.getCreatedAt()
        );
    }
}
