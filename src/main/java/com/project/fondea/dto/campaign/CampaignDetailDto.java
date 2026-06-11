package com.project.fondea.dto.campaign;

import com.project.fondea.dto.faq.FaqDto;
import com.project.fondea.dto.rewards.RewardSummaryDto;
import com.project.fondea.model.enums.CampaignStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record CampaignDetailDto(
        UUID id,
        String title,
        String description,
        String coverImageUrl,
        String creatorName,
        UUID creatorId,
        BigDecimal goalAmount,
        BigDecimal totalPledged,
        int pledgeCount,
        int daysLeft,
        LocalDate deadline,
        LocalDateTime createdAt,
        Boolean isFlexibleGoal,
        CampaignStatus status,
        UUID categoryId,
        UUID locationId,
        String country,
        String city,
        List<RewardSummaryDto> rewards,
        List<FaqDto> faqs,
        String rejectionReason
) {
}
