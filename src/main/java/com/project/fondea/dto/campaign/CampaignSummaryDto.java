package com.project.fondea.dto.campaign;

import com.project.fondea.model.enums.CampaignStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record CampaignSummaryDto(
        UUID id,
        String title,
        String creatorName,
        BigDecimal goalAmount,
        BigDecimal totalPledged,    // cuánto llevan recaudado
        int pledgeCount,            // cuántos patrocinadores tiene
        LocalDate deadline,
        String categoryName,
        String locationCity,
        CampaignStatus status,
        Double featuredScore
) {
}
