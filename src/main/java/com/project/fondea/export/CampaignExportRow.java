package com.project.fondea.export;

import com.project.fondea.model.enums.CampaignStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record CampaignExportRow(
        UUID id,
        String title,
        String creatorName,
        String categoryName,
        String locationCity,
        BigDecimal goalAmount,
        BigDecimal pendingPledged,
        BigDecimal chargedPledged,
        BigDecimal totalPledged,
        long pledgeCount,
        LocalDate deadline,
        CampaignStatus status,
        Double featuredScore
) {
}
