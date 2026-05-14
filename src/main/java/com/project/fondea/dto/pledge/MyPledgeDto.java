package com.project.fondea.dto.pledge;

import com.project.fondea.model.enums.PledgeStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record MyPledgeDto(
        UUID id,
        UUID campaignId,
        String campaignTitle,
        String creatorName,
        String rewardTitle,
        BigDecimal amount,
        PledgeStatus status,
        LocalDateTime createdAt,
        BigDecimal campaignGoal,
        BigDecimal campaignTotalPledged,
        LocalDate campaignDeadline
) {}