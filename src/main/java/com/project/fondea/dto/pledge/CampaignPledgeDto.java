package com.project.fondea.dto.pledge;

import com.project.fondea.model.enums.PledgeStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record CampaignPledgeDto(
        UUID id,
        UUID sponsorId,
        String sponsorName,
        BigDecimal amount,
        PledgeStatus status,
        LocalDateTime createdAt
) {}