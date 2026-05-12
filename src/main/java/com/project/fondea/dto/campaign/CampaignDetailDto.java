package com.project.fondea.dto.campaign;

import com.project.fondea.dto.faq.FaqDto;
import com.project.fondea.dto.rewards.RewardSummaryDto;
import com.project.fondea.model.enums.CampaignStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record CampaignDetailDto(
        UUID id,
        String title,
        String description,          // texto completo
        String creatorName,
        UUID creatorId,
        BigDecimal goalAmount,
        BigDecimal totalPledged,
        int pledgeCount,
        int daysLeft,                // calculado: deadline - hoy
        LocalDate deadline,
        Boolean isFlexibleGoal,
        CampaignStatus status,
        String categoryName,
        String locationCity,
        String locationCountry,
        List<RewardSummaryDto> rewards,   // recompensas disponibles
        List<FaqDto> faqs                 // preguntas frecuentes
) {
}
